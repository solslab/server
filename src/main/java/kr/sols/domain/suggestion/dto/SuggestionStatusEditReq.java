package kr.sols.domain.suggestion.dto;

import kr.sols.domain.suggestion.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionStatusEditReq {
    Status status;
}
