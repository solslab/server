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
    private SocialType socialType;
    private String createdDate;
}