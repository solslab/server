package kr.sols.auth.dto;
import static kr.sols.exception.ErrorCode.ILLEGAL_REGISTRATION_ID;
import kr.sols.auth.exception.AuthException;
import kr.sols.common.KeyGenerator;
import kr.sols.common.NickNameGenerator;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.entity.Role;
import java.util.Map;

import kr.sols.domain.member.entity.SocialType;
import lombok.Builder;

@Builder
public record OAuth2UserInfo(
        String name,
        String email
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
//            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

//    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
//        return OAuth2UserInfo.builder()
//                .name((String) attributes.get("name"))
//                .email((String) attributes.get("email"))
//                .profile((String) attributes.get("picture"))
//                .build();
//    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .build();
    }

    public Member toEntity(String registrationId) {
        SocialType socialType = switch (registrationId) {
            case "kakao" -> SocialType.KAKAO;
            // case "google" -> SocialType.GOOGLE;
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };

        return Member.builder()
                .name(name)
                .nickname(NickNameGenerator.generateRandomNickname())
                .email(email)
                .socialType(socialType)
                .memberKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .build();
    }
}