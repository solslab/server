package kr.sols.domain.position.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PositionTabDto {
    private String loginStatus;
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

    public static PositionTabDto fromDto(PositionDto position, boolean isMember) {
        return PositionTabDto.builder()
                .loginStatus(isMember ? "signed" : "unsigned")
                .positionName(position.getPositionName())
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
