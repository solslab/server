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
public class PositionDto {
    private UUID positionId;
    private String positionName;
    private Boolean isOfficial;
    private List<String> supportLanguages;
    private String testTime;
    private String problemInfo;
    private String permitIde;
    private String permitSearch;
    private String hiddenCase;
    private String examMode;
    private String testPlace;
    private String note;

    public static PositionDto fromEntity(Position position) {
        return PositionDto.builder()
                .positionId(position.getId())
                .positionName(position.getPositionName())
                .isOfficial(position.getIsOfficial())
                .supportLanguages(position.getSupportLanguages())
                .testTime(position.getTestTime())
                .problemInfo(position.getProblemInfo())
                .permitIde(position.getPermitIde())
                .permitSearch(position.getPermitSearch())
                .hiddenCase(position.getHiddenCase())
                .examMode(position.getExamMode())
                .testPlace(position.getTestPlace())
                .note(position.getNote())
                .build();
    }
}
