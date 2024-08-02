package kr.sols.member.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MemberEditRequest {
    private Integer memberTier;
    private List<String> preferLanguages;
    private List<String> preferPositions;
    private List<String> preferIndustries;
}
