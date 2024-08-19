package kr.sols.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import kr.sols.common.StringListConverter;
import kr.sols.domain.member.dto.MemberEditRequest;
import kr.sols.common.BaseTimeEntity;
//import jakarta.persistence.Embedded;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 20, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String memberKey;

    @Convert(converter = StringListConverter.class)
    private List<String> preferLanguages;

    @Enumerated(value = EnumType.STRING)
    private AlPlatform alPlatform;

    @Min(0)
    @Max(36)
    private Integer memberTier;

//    @Convert(converter = StringListConverter.class)
//    private List<String> preferPositions;

    @Convert(converter = StringListConverter.class)
    private List<String> preferIndustries;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Setter
    @Column(nullable = false, unique = true)
    private String tid;


    @Builder
    public Member(String name, String nickname, String email, SocialType socialType, List<String> preferLanguages, AlPlatform alPlatform, Integer memberTier, List<String> preferIndustries, String memberKey, Role role, String tid) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.socialType = socialType;
        this.alPlatform = alPlatform;
        this.memberTier = memberTier;
        this.preferLanguages = preferLanguages;
//        this.preferPositions = preferPositions;
        this.preferIndustries = preferIndustries;
        this.memberKey = memberKey;
        this.role = role;
        this.tid = tid;
    }

    public void updateMember(MemberEditRequest request) {
        if (request.getNickname() != null) {
            this.nickname = request.getNickname();
        }
        if (request.getAlPlatform() != null) {
            this.alPlatform = request.getAlPlatform();
        }
        if (request.getMemberTier() != null) {
            this.memberTier = request.getMemberTier();
        }
        if (request.getPreferLanguages() != null) {
            this.preferLanguages = request.getPreferLanguages();
        }
//        if (request.getPreferPositions() != null) {
//            this.preferPositions = request.getPreferPositions();
//        }
        if (request.getPreferIndustries() != null) {
            this.preferIndustries = request.getPreferIndustries();
        }
    }
}