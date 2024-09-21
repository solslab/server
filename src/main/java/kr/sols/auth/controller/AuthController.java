package kr.sols.auth.controller;

//import kr.sols.notification.service.RedisMessageService;
import kr.sols.auth.dto.CheckTokenResponse;
import kr.sols.auth.dto.LoginResponse;
import kr.sols.auth.jwt.TokenProvider;
import kr.sols.auth.service.AuthService;
import kr.sols.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    // TokenService 빈 주입
    private final TokenService tokenService;
    private final AuthService authService;

    @GetMapping("/auth/success")
    public ResponseEntity<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        // 로그인 성공 응답을 생성하여 반환
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        // 리프레시 토큰 삭제
        tokenService.deleteRefreshToken(userDetails.getUsername());

        // 로그아웃 성공 응답 반환 (204 No Content)
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/check")
    public ResponseEntity<CheckTokenResponse> checkTokenAndRefresh(@RequestHeader("Authorization") String accessToken) {
        // "Bearer " 접두사 제거
        String token = accessToken.replace("Bearer ", "");

        // 토큰 값으로 서비스 호출
        return ResponseEntity.ok(authService.checkTokenAndRefresh(token));
    }

    @GetMapping("/login")
    public String getHtmlPage() {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>로그인</title>" +
                "</head>" +
                "<body>" +
                "<a href=\"/oauth2/authorization/kakao\" id=\"kakao-login-btn\">카카오 로그인</a>" +
                "</body>" +
                "</html>";
    }
}