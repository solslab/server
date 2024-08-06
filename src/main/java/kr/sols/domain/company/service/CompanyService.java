package kr.sols.domain.company.service;

import kr.sols.domain.company.dto.CompanyDto;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.dto.CompanyLogoDto;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.COMPANY_NOT_FOUND;
import static kr.sols.exception.ErrorCode.DUPLICATED_COMPANY_NAME;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final S3Service s3Service;
    private final CompanyRepository companyRepository;

    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto) {
        if (companyRepository.existsByCompanyName(companyDto.getCompanyName())) {
            throw new CompanyException(DUPLICATED_COMPANY_NAME);
        }
            Company company = Company.builder()
                .companyName(companyDto.getCompanyName())
                .industryType(companyDto.getIndustryType())
                .companyLogo(companyDto.getCompanyLogo())
                .build();

        Company savedCompany = companyRepository.save(company);
        return CompanyDto.fromEntity(savedCompany);
    }

    public List<CompanyDto> getAllCompanies() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream()
                .map(CompanyDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompanyDto getCompany(UUID id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map(CompanyDto::fromEntity).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
    }

    @Transactional
    public CompanyDto updateCompany(UUID id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        company.setCompanyName(companyDto.getCompanyName());
        company.setIndustryType(companyDto.getIndustryType());

        Company updatedCompany = companyRepository.save(company);
        return CompanyDto.fromEntity(updatedCompany);
    }

    @Transactional
    public void deleteCompany(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyException(COMPANY_NOT_FOUND);
        }
        // S3에서 로고 이미지 삭제
        Optional<CompanyLogoDto> targetLogoUrlOpt = companyRepository.findCompanyLogoById(id);
        targetLogoUrlOpt.ifPresent(targetLogoUrl -> s3Service.deleteFile(targetLogoUrl.getCompanyLogo()));

        companyRepository.deleteById(id);
    }

    @Transactional
    public String uploadCompanyLogo(UUID id, String logoUrl) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));

        // 이미 로고가 있다면 S3에서 삭제
        Optional<CompanyLogoDto> targetLogoUrlOpt = companyRepository.findCompanyLogoById(id);
        targetLogoUrlOpt.ifPresent(targetLogoUrl -> s3Service.deleteFile(targetLogoUrl.getCompanyLogo()));

        company.setCompanyLogo(logoUrl); // 로고 등록(교체)
        companyRepository.save(company);
        return logoUrl;
    }

    @Transactional
    public void deleteCompanyLogo(UUID id) {
        // S3에서 삭제
        Optional<CompanyLogoDto> targetLogoUrlOpt = companyRepository.findCompanyLogoById(id);
        targetLogoUrlOpt.ifPresent(targetLogoUrl -> s3Service.deleteFile(targetLogoUrl.getCompanyLogo()));

        // DB에서 삭제
        Company company = companyRepository.findById(id).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        company.setCompanyLogo(null);

        companyRepository.save(company);
    }
}
