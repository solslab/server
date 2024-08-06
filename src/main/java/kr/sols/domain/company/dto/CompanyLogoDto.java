package kr.sols.domain.company.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyLogoDto {
    private String companyLogo;

    public CompanyLogoDto() {
    }

    public CompanyLogoDto(String companyLogo) {
        this.companyLogo = companyLogo;
    }
}
