package kr.sols.domain.member.dto;
import kr.sols.domain.member.entity.SocialType;
import kr.sols.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
//    private String memberKey;
    private String name;
    private String email;
    private Integer memberTier;
    private List<String> preferLanguages;
//    private List<String> preferPositions;
    private List<String> preferIndustries;
    private SocialType socialType;

    public static MemberDto fromEntity(Member member) {
        MemberDto memberDto = MemberDto.builder()
//                .memberKey(member.getMemberKey())
                .name(member.getName())
                .email(member.getEmail())
                .memberTier(member.getMemberTier())
                .preferLanguages(member.getPreferLanguages())
//                .preferPositions(member.getPreferPositions())
                .preferIndustries(member.getPreferIndustries())
                .socialType(member.getSocialType())
                .build();

        return memberDto;
    }
}