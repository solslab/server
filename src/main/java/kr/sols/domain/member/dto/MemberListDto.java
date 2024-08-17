package kr.sols.domain.member.dto;
import kr.sols.domain.company.dto.CompanyListDto;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static kr.sols.common.FormatDate.formatDate;

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

    public static MemberListDto fromEntity(Member member) {
        return MemberListDto.builder()
                .memberKey(member.getMemberKey())
                .name(member.getName())
                .email(member.getEmail())
                .socialType(member.getSocialType())
                .createdDate(formatDate(member.getCreatedDate()))
                .build();
    }
}