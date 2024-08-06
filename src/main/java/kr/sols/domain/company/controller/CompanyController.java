package kr.sols.domain.company.controller;

import kr.sols.common.TypeValidator;
import kr.sols.domain.company.dto.*;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.service.CompanyService;
import kr.sols.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static kr.sols.exception.ErrorCode.INVALID_INDUSTRY_TYPE;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final S3Service s3Service;


    // 기업 생성
    @PostMapping
    public ResponseEntity<CompanyIdDto> createCompany(@RequestBody CompanyRequestDto companyRequestDto) {
        // 검증 로직
        if (!TypeValidator.isValidIndustryTypeList(companyRequestDto.getIndustryType())) {
            throw new CompanyException(INVALID_INDUSTRY_TYPE);
        }

        // 회사 생성 로직
        CompanyIdDto createdCompany = companyService.createCompany(companyRequestDto);
        return ResponseEntity.ok(createdCompany);
    }

    // 기업 전체 조회
    @GetMapping
    public List<CompanyListDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    // 기업 수정
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDetailDto> updateCompany(@PathVariable UUID id, @RequestBody CompanyRequestDto companyRequestDto) {
        CompanyDetailDto updatedCompany = companyService.updateCompany(id, companyRequestDto);
        return ResponseEntity.ok(updatedCompany);
    }

    // 기업 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // 기업 로고 생성(변경)
    @PostMapping(path = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompanyLogoDto> uploadCompanyLogo(@PathVariable UUID id,
                                                            @RequestPart(value = "fileName") String fileName,
                                                            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        String extend = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        return ResponseEntity.ok(companyService.uploadCompanyLogo(id, fileName, multipartFile, extend));
    }

    // 기업 로고 삭제
    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteCompanyLogo(@PathVariable UUID id) {
        companyService.deleteCompanyLogo(id);
        return ResponseEntity.noContent().build();
    }

    // 기업 조회
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDetailDto> getCompany(@PathVariable UUID id) {
        CompanyDetailDto company = companyService.getCompany(id);
        return ResponseEntity.ok(company);
    }

}
