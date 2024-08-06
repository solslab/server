package kr.sols.domain.company.dto;

import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.entity.IndustryType;
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
public class CompanyDto {
    private UUID id;
    private String companyName;
    private List<String> industryType;
    private String companyLogo;

    public static CompanyDto fromEntity(Company company) {
        CompanyDto companyDto = CompanyDto.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .industryType(company.getIndustryType())
                .build();

        return companyDto;
    }
}