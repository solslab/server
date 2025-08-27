package kr.sols.domain.company.dto;

import kr.sols.common.file.LocalFileService;
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
    private List<String> industryType;

    public static CompanyListDto fromEntity(Company company, LocalFileService fileService) {
        String logoUrl = null;
        String storedFilename = company.getCompanyLogo();

        if (storedFilename != null && !storedFilename.isBlank()) {
            logoUrl = fileService.getCompanyLogoFullPath(storedFilename);
        }

        return CompanyListDto.builder()
                .companyId(company.getId())
                .companyName(company.getCompanyName())
                .companyLogo(logoUrl) // 변환된 URL을 세팅
                .industryType(company.getIndustryType())
                .build();
    }
}
