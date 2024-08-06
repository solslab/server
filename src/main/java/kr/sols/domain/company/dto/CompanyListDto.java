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
public class CompanyListDto {
    private UUID companyId;
    private String companyName;
    private String companyLogo;

    public static CompanyListDto fromEntity(Company company) {
        return CompanyListDto.builder()
                .companyId(company.getId())
                .companyName(company.getCompanyName())
                .companyLogo(company.getCompanyLogo())
                .build();
    }
}