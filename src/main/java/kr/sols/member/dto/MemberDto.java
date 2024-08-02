package kr.sols.member.dto;
import kr.sols.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String name;
    private String email;

    public static MemberDto fromEntity(Member member) {
        MemberDto memberDto = MemberDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();

        return memberDto;
    }
}