package kr.sols.domain.company.dto;

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
    private List<PositionListDto> positions; // PositionListDto 목록 추가

    public static CompanyDetailDto fromEntity(Company company, List<PositionListDto> positions) {
        return CompanyDetailDto.builder()
                .companyName(company.getCompanyName())
                .industryType(company.getIndustryType()) // 필요에 따라 변환 가능
                .companyLogo(company.getCompanyLogo())   // 필요에 따라 변환 가능
                .positions(positions) // 외부에서 받은 PositionListDto 목록 설정
                .build();
    }
}