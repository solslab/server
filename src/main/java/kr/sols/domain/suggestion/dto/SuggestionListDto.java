package kr.sols.domain.suggestion.dto;

import kr.sols.common.FormatDate;
import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.suggestion.entity.Status;
import kr.sols.domain.suggestion.entity.Suggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionListDto {
    UUID suggestionId;
    String companyName;
    String memberName;
    Status status;
    String createdDate;

    public static SuggestionListDto fromEntity(Suggestion suggestion, String companyName, String memberName) {
        return SuggestionListDto.builder()
                .suggestionId(suggestion.getId())
                .companyName(companyName)
                .memberName(memberName)
                .status(suggestion.getSuggestionStatus())
                .createdDate(FormatDate.formatDate(suggestion.getCreatedDate()))
                .build();
    }
}
