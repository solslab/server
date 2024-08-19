package kr.sols.domain.member.dto;

import kr.sols.domain.member.entity.AlPlatform;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MemberEditRequest {
    private String nickname;
    private AlPlatform alPlatform;
    private Integer memberTier;
    private List<String> preferLanguages;
//    private List<String> preferPositions;
    private List<String> preferIndustries;
}
