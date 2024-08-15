package kr.sols.domain.company.service;

import kr.sols.domain.company.dto.*;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.position.repository.PositionRepository;
import kr.sols.domain.position.service.PositionService;
import kr.sols.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.COMPANY_NOT_FOUND;
import static kr.sols.exception.ErrorCode.DUPLICATED_COMPANY_NAME;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final S3Service s3Service;
    private final CompanyRepository companyRepository;
    private final PositionService positionService;
    private final PositionRepository positionRepository;

    @Transactional
    public CompanyCreatedResponse createCompany(CompanyRequestDto companyRequestDto) {
        if (companyRepository.existsByCompanyName(companyRequestDto.getCompanyName())) {
            throw new CompanyException(DUPLICATED_COMPANY_NAME);
        }
        Company company = Company.builder()
                .companyName(companyRequestDto.getCompanyName())
                .industryType(companyRequestDto.getIndustryType())
                .build();

        Company savedCompany = companyRepository.save(company);
        return new CompanyCreatedResponse(savedCompany.getId());
    }

    @Transactional(readOnly = true)
    public List<CompanyListDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAllByOrderByCompanyNameAsc();
        return companies.stream()
                .map(CompanyListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompanyDetailDto getCompany(UUID id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        List<PositionListDto> positions = positionService.getAllPositionOfCompany(id);

        return CompanyDetailDto.fromEntity(company, positions);
    }

    @Transactional
    public CompanyCreatedResponse updateCompany(UUID id, CompanyRequestDto companyRequestDto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        company.setCompanyName(companyRequestDto.getCompanyName());
        company.setIndustryType(companyRequestDto.getIndustryType());

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
        // 404 처리
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        // 이미 로고가 있다면 S3에서 삭제
        if (company.getCompanyLogo() != null) {
            s3Service.deleteFile(company.getCompanyLogo());
        }

        // 로고 등록(교체)
        String url = s3Service.upload(fileName, multipartFile, extend);
        company.setCompanyLogo(url);
        companyRepository.save(company);

        // DTO로 반환
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

    @Transactional
    public List<CompanyListDto> searchCompanies(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return List.of();
        }

        List<Company> companies = companyRepository.findByCompanyNameStartingWith(searchTerm);

        return companies.stream()
                .map(CompanyListDto::fromEntity)
                .collect(Collectors.toList());
    }
}
