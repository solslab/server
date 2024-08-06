package kr.sols.domain.company.dto;

import kr.sols.domain.company.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailDto {
    private String companyName;
    private List<String> industryType;
    private String companyLogo;

    public static CompanyDetailDto fromEntity(Company company) {
        return CompanyDetailDto.builder()
                .companyName(company.getCompanyName())
                .industryType(company.getIndustryType()) // 필요에 따라 변환할 수 있음
                .companyLogo(company.getCompanyLogo()) // 필요에 따라 변환할 수 있음
                .build();
    }
}