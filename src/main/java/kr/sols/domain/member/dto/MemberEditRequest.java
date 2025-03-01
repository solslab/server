package kr.sols.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class MemberEditRequest {
    private String nickname;
    private Integer memberTier;
    private List<String> preferLanguages;
    private List<String> preferIndustries;
}
