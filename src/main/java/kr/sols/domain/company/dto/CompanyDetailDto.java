package kr.sols.domain.company.dto;

import kr.sols.common.file.LocalFileService;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.position.dto.PositionListDto;
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
    private List<String> searchTerms;
    private boolean isPublic;
    private List<PositionListDto> positions;

    public static CompanyDetailDto fromEntity(Company company, List<PositionListDto> positions, LocalFileService fileService) {
        String logoUrl = null;
        String storedFilename = company.getCompanyLogo();

        if (storedFilename != null && !storedFilename.isBlank()) {
            logoUrl = fileService.getCompanyLogoFullPath(storedFilename);
        }

        return CompanyDetailDto.builder()
                .companyName(company.getCompanyName())
                .industryType(company.getIndustryType())
                .companyLogo(logoUrl) // 변환된 URL을 세팅
                .searchTerms(company.getSearchTerms())
                .isPublic(company.isPublic())
                .positions(positions)
                .build();
    }
}
