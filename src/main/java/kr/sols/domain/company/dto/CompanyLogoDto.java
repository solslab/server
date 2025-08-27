package kr.sols.domain.company.dto;

import kr.sols.common.file.dto.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLogoDto {
    private String companyLogo;
}
