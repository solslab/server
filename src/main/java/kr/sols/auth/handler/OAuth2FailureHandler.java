package kr.sols.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private static final String URI = "http://localhost:3001/api/login/fail";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 login fail. ", exception);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "소셜 로그인에 실패하였습니다.");

        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("message", exception)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}