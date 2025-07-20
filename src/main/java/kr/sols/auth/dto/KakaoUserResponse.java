package kr.sols.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.entity.Role;
import kr.sols.domain.member.entity.SocialType;
import kr.sols.common.KeyGenerator;
import kr.sols.common.NickNameGenerator;
import lombok.Data;

@Data
public class KakaoUserResponse {

    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public Member toMember() {
        return Member.builder()
                .email(getEmail())
                .name(kakaoAccount.getProfile().getNickname())
                .nickname(NickNameGenerator.generateRandomNickname())
                .socialType(SocialType.KAKAO)
                .memberKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .tid(String.valueOf(id))
                .build();
    }

    @Data
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Data
        public static class Profile {
            private String nickname;
        }
    }
}
