package kr.sols.domain.position.dto;

import kr.sols.domain.position.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PositionListDto {
    private UUID positionId;
    private String positionName;

    public static PositionListDto fromEntity(Position position) {
        return PositionListDto.builder()
                .positionId(position.getId())
                .positionName(position.getPositionName())
                .build();
    }
}
