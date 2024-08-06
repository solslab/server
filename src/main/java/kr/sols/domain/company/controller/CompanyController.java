package kr.sols.domain.company.controller;

import kr.sols.common.TypeValidator;
import kr.sols.domain.company.dto.CompanyDto;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.service.CompanyService;
import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        // 검증 로직
        if (!TypeValidator.isValidIndustryTypeList(companyDto.getIndustryType())) {
            throw new CompanyException(INVALID_INDUSTRY_TYPE);
        }

        // 회사 생성 로직
        CompanyDto createdCompany = companyService.createCompany(companyDto);
        return ResponseEntity.ok(createdCompany);
    }

    // 기업 전체 조회
    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        return companyService.getAllCompanies(); }

    // 기업 수정
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable UUID id, @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
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
    public ResponseEntity<String> uploadCompanyLogo(@PathVariable UUID id,
                                                    @RequestPart(value = "fileName") String fileName,
                                                    @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        String extend = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String url = s3Service.upload(fileName, multipartFile, extend);
        companyService.uploadCompanyLogo(id, url);

        String responseBody = "{\"imageUrl\": \"" + url + "\"}";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    // 기업 로고 삭제
    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteCompanyLogo(@PathVariable UUID id) {
        companyService.deleteCompanyLogo(id);
        return ResponseEntity.noContent().build();
    }

    // 기업 조회
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompany(@PathVariable UUID id) {
        CompanyDto company = companyService.getCompany(id);
        return ResponseEntity.ok(company);
    }

}
