package kr.sols.auth.jwt;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("토큰추출시도");
        System.out.println(request.getHeader("Authorization"));
        // 요청에서 JWT 토큰을 추출합니다.
        String accessToken = resolveToken(request);
        System.out.println(accessToken);
        System.out.println("토큰추출끝");
        // 토큰이 유효한지 검증합니다.
        if (tokenProvider.validateToken(accessToken)) {
            // 토큰이 유효하다면 인증 정보를 설정합니다.
            setAuthentication(accessToken);
        } else {
            // 토큰이 유효하지 않다면, 리프레시 토큰으로 새로운 액세스 토큰을 발급합니다.
            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);

            if (StringUtils.hasText(reissueAccessToken)) {
                // 새로운 액세스 토큰으로 인증 정보를 설정합니다.
                setAuthentication(reissueAccessToken);
                // 응답 헤더에 새로운 액세스 토큰을 설정합니다.
                response.setHeader(AUTHORIZATION, "Bearer " + reissueAccessToken);
            }
        }

        // 필터 체인을 계속 진행합니다.
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            return null;
        }
        // "Bearer " 접두사를 제거하고 토큰만 반환합니다.
        return token.substring("Bearer ".length());
    }
}

