package kr.sols.domain.suggestion.dto;

import kr.sols.common.FormatDate;
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
public class SuggestionDto {
    UUID suggestionId;
    String companyName;
    String positionName;
    String suggestionContent;
    String memberName;
    String memberEmail;
    Status status;
    String createdDate;

    public static SuggestionDto fromEntity(Suggestion suggestion, String companyName, String positionName, String memberName, String memberEmail) {
        return SuggestionDto.builder()
                .suggestionId(suggestion.getId())
                .companyName(companyName)
                .positionName(positionName)
                .suggestionContent(suggestion.getSuggestionContent())
                .memberName(memberName)
                .memberEmail(memberEmail)
                .status(suggestion.getSuggestionStatus())
                .createdDate(FormatDate.formatDate(suggestion.getCreatedDate()))
                .build();
    }
}
