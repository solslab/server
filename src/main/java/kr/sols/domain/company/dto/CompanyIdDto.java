package kr.sols.domain.company.dto;

import kr.sols.domain.company.entity.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CompanyIdDto {
    private UUID companyId;

    public CompanyIdDto(UUID companyId) {
        this.companyId = companyId;
    }

    public static CompanyIdDto fromEntity(Company company) {
        return CompanyIdDto.builder()
                .companyId(company.getId())
                .build();
    }
}