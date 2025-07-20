package kr.sols.auth.service;

import kr.sols.auth.dto.KakaoTokenResponse;
import kr.sols.auth.dto.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final WebClient webClient;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String userInfoUri;

    public String getAccessToken(String code, String redirectUri) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);
        formData.add("client_secret", clientSecret);

        System.out.println("리다이렉트: " + redirectUri);
        KakaoTokenResponse response = webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (response == null || response.getAccessToken() == null) {
            throw new IllegalStateException("카카오 액세스 토큰 발급 실패");
        }

        return response.getAccessToken();
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        KakaoUserResponse response = webClient.get()
                .uri(userInfoUri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("카카오 사용자 정보 요청 실패");
        }

        return response;
    }
}
