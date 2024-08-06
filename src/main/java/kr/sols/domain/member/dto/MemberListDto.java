package kr.sols.domain.member.dto;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberListDto {
    private String memberKey;
    private String name;
    private String email;
//    private Integer memberTier;
//    private List<String> preferLanguages;
//    private List<String> preferPositions;
//    private List<String> preferIndustries;
    private SocialType socialType;
    private String createdDate;

    public static MemberListDto fromEntity(Member member) {
        String formattedDate = member.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        MemberListDto memberListDto = MemberListDto.builder()
                .memberKey(member.getMemberKey())
                .name(member.getName())
                .email(member.getEmail())
//                .memberTier(member.getMemberTier())
//                .preferLanguages(member.getPreferLanguages())
//                .preferPositions(member.getPreferPositions())
//                .preferIndustries(member.getPreferIndustries())
                .socialType(member.getSocialType())
                .createdDate(formattedDate)
                .build();

        return memberListDto;
    }
}