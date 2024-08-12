package kr.sols.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import kr.sols.auth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    // TokenProvider 빈 주입
    private final TokenProvider tokenProvider;

    private static final String URI = "http://localhost:3001/api/login/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 액세스 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // 리프레시 토큰 생성
        tokenProvider.generateRefreshToken(authentication, accessToken);

        // 리다이렉션 URL 생성 시 쿼리 파라미터로 액세스 토큰 포함
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        // 생성된 URL로 리다이렉트
        response.sendRedirect(redirectUrl);
    }
}