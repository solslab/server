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

        String accessToken = resolveToken(request);
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            // 토큰이 유효하지 않다면, 리프레시 토큰으로 새로운 액세스 토큰을 발급
            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
            if (StringUtils.hasText(reissueAccessToken)) {
                // 새로운 액세스 토큰으로 인증 정보 설정
                setAuthentication(reissueAccessToken);
                response.setHeader(AUTHORIZATION, "Bearer " + reissueAccessToken);
            }
        }
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
        return token.substring("Bearer ".length());
    }
}

