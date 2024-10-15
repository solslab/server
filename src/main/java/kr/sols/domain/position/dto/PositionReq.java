package kr.sols.domain.position.dto;

import kr.sols.domain.position.entity.Position;
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

public class PositionReq {
    private String positionName;
    private List<String> supportLanguages;
    private String testTime;
    private String problemInfo;
    private String permitIde;
    private String permitSearch;
    private String hiddenCase;
    private String examMode;
    private String testPlace;
    private String note;
    private Boolean isOfficial;
}
