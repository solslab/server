package kr.sols.domain.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyIdDto {
    private UUID companyId;

    public CompanyIdDto() {
    }

    public CompanyIdDto(UUID companyId) {
        this.companyId = companyId;
    }
}