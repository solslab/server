package kr.sols.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 인증 예외
        String requestURI = request.getRequestURI();
        if (isPathExcluded(requestURI)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        log.error("AuthenticationException is occurred. ", authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패하였습니다.");
    }

    private boolean isPathExcluded(String requestURI) {
        // 특정 URL 패턴을 패스하도록 설정
        return requestURI.startsWith("/tab/testInfo");
    }
}