package kr.sols.domain.member.dto;
import kr.sols.domain.member.entity.AlPlatform;
import kr.sols.domain.member.entity.SocialType;
import kr.sols.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static kr.sols.common.FormatDate.formatDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
//    private String memberKey;
    private String name;
    private String nickname;
    private String email;
    private AlPlatform alPlatform;
    private Integer memberTier;
    private List<String> preferLanguages;
//    private List<String> preferPositions;
    private List<String> preferIndustries;
    private SocialType socialType;
    private String createdDate;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .alPlatform(member.getAlPlatform())
                .memberTier(member.getMemberTier())
                .preferLanguages(member.getPreferLanguages())
//                .preferPositions(member.getPreferPositions())
                .preferIndustries(member.getPreferIndustries())
                .socialType(member.getSocialType())
                .createdDate(formatDate(member.getCreatedDate()))
                .build();
    }
}