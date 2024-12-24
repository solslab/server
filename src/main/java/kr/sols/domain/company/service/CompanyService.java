package kr.sols.domain.company.service;

import kr.sols.domain.company.dto.*;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.position.entity.Position;
import kr.sols.domain.position.repository.PositionRepository;
import kr.sols.domain.position.service.PositionService;
import kr.sols.domain.testReview.repository.TestReviewRepository;
import kr.sols.domain.testReview.service.TestReviewService;
import kr.sols.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final S3Service s3Service;
    private final CompanyRepository companyRepository;
    private final PositionRepository positionRepository;
    private final TestReviewRepository testReviewRepository;

    @Transactional
    public CompanyCreatedResponse createCompany(CompanyRequest request) {
        if (companyRepository.existsByCompanyName(request.getCompanyName())) {
            throw new CompanyException(DUPLICATED_COMPANY_NAME);
        }
        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .industryType(request.getIndustryType())
                .searchTerms(request.getSearchTerms())
                .isPublic(request.isPublic())
                .build();

        Company savedCompany = companyRepository.save(company);
        return new CompanyCreatedResponse(savedCompany.getId());
    }

    @Transactional(readOnly = true)
    public CompanyPageDto getAllCompanies(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Company> companyPage = companyRepository.findAllByIsPublicTrueOrderByCompanyNameAsc(pageable);

        int totalPageNum = companyPage.getTotalPages();
        int currentPageNum = companyPage.getNumber() + 1;


        if (currentPageNum > totalPageNum || currentPageNum < 1) throw new CompanyException(PAGE_NOT_FOUND);

        List<CompanyListDto> companies = companyPage.getContent()
                .stream()
                .map(CompanyListDto::fromEntity)
                .toList();

        return CompanyPageDto.builder()
                .companies(companies)
                .totalElements((int) companyPage.getTotalElements())
                .totalPages(totalPageNum)
                .currentPage(currentPageNum)
                .pageSize(pageSize)
                .build();
    }

    @Transactional(readOnly = true)
    public CompanyPageDto getAllPrivateCompanies(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Company> companyPage = companyRepository.findAllByIsPublicFalseOrderByCompanyNameAsc(pageable);

        int totalPageNum = companyPage.getTotalPages();
        int currentPageNum = companyPage.getNumber() + 1;


        if (currentPageNum > totalPageNum || currentPageNum < 1) throw new CompanyException(PAGE_NOT_FOUND);

        List<CompanyListDto> companies = companyPage.getContent()
                .stream()
                .map(CompanyListDto::fromEntity)
                .toList();

        return CompanyPageDto.builder()
                .companies(companies)
                .totalElements((int) companyPage.getTotalElements())
                .totalPages(totalPageNum)
                .currentPage(currentPageNum)
                .pageSize(pageSize)
                .build();
    }

    @Transactional(readOnly = true)
    public CompanyDetailDto getCompany(UUID id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
//        if (!company.isPublic()) {
//            throw new CompanyException(COMPANY_NOT_FOUND);
//        }
        List<Position> positions = positionRepository.findAllByCompanyIdOrderByCreatedDateDesc(id);
        List<PositionListDto> positionDtos = positions.stream()
                .map(PositionListDto::fromEntity)
                .collect(Collectors.toList());

        return CompanyDetailDto.fromEntity(company, positionDtos);
    }

    @Transactional
    public CompanyCreatedResponse updateCompany(UUID id, CompanyRequest request) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        company.setCompanyName(request.getCompanyName());
        company.setIndustryType(request.getIndustryType());
        company.setSearchTerms(request.getSearchTerms());
        company.setPublic(request.isPublic());

        Company updatedCompany = companyRepository.save(company);
        return new CompanyCreatedResponse(updatedCompany.getId());
    }

    @Transactional
    public void deleteCompany(UUID id) {
        // 404 처리
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        // 직무가 있다면 삭제
        positionRepository.deleteAllByCompanyId(id);

        // 로고가 있다면 S3에서 삭제
        if (company.getCompanyLogo() != null) {
            s3Service.deleteFile(company.getCompanyLogo());
        }

        companyRepository.deleteById(id);
    }

    @Transactional
    public CompanyLogoDto uploadCompanyLogo(UUID id, String fileName, MultipartFile multipartFile, String extend) throws IOException {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        // 이미 로고가 있다면 S3에서 삭제
        if (company.getCompanyLogo() != null) {
            s3Service.deleteFile(company.getCompanyLogo());
        }

        // 로고 등록(교체)
        String url = s3Service.upload(fileName, multipartFile, extend);
        company.setCompanyLogo(url);
        companyRepository.save(company);

        return new CompanyLogoDto(url);
    }

    @Transactional
    public void deleteCompanyLogo(UUID id) {
        // 404 처리
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        // 로고가 있다면 삭제
        if (company.getCompanyLogo() != null) {
            s3Service.deleteFile(company.getCompanyLogo()); // S3
            company.setCompanyLogo(null); // DB
        }

        companyRepository.save(company);
    }

    // 등록기업 검색
    @Transactional(readOnly = true)
    public List<CompanyListDto> searchCompanies(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        List<Company> companies = companyRepository.searchCompanyByTerm(searchTerm);
        return companies.stream()
                .map(CompanyListDto::fromEntity)
                .toList();
    }

    // 국내기업 전체검색
    @Transactional(readOnly = true)
    public List<CompanyListDto> searchAllCompanies(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        List<Company> companies = companyRepository.searchAllCompanyByTerm(searchTerm);
        return companies.stream()
                .map(CompanyListDto::fromEntity)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<CompanyListDto> getRandomCompaniesForHome() {
        List<Company> companies = companyRepository.findRandomCompaniesForHome();

        return companies.stream()
                .map(CompanyListDto::fromEntity)
                .toList();

    }

    @Transactional
    public void updateCompanyStatus(UUID companyId) {
        long reviewCount = testReviewRepository.countByCompanyId(companyId);
        long positionCount = positionRepository.countByCompanyId(companyId);
        System.out.println(reviewCount);
        System.out.println(positionCount);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        company.updatePublicStatus(reviewCount, positionCount);
        companyRepository.save(company);
    }
}
