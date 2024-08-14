package kr.sols.domain.company.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CompanyCreatedResponse {
    private UUID companyId;

    public CompanyCreatedResponse() {
    }

    public CompanyCreatedResponse(UUID companyId) {
        this.companyId = companyId;
    }
}