package kr.sols.domain.suggestion.dto;

import kr.sols.domain.suggestion.entity.Status;
import lombok.Getter;

@Getter
public class SuggestionStatusEditReq {
    Status status;
}
