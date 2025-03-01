package kr.sols.domain.member.dto;
import kr.sols.domain.member.entity.SocialType;
import kr.sols.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static kr.sols.common.FormatDate.formatDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String name;
    private String nickname;
    private String email;
    private Integer memberTier;
    private List<String> preferLanguages;
    private List<String> preferIndustries;
    private SocialType socialType;
    private String createdDate;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .memberTier(member.getMemberTier())
                .preferLanguages(parseList(member.getPreferLanguages()))
                .preferIndustries(parseList(member.getPreferIndustries()))
                .socialType(member.getSocialType())
                .createdDate(formatDate(member.getCreatedDate()))
                .build();
    }

    private static List<String> parseList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        // 모든 요소가 빈 문자열인지 검사
        boolean allEmpty = values.stream().allMatch(String::isEmpty);

        return allEmpty ? null : values;
    }

}