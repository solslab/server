package kr.sols.auth.controller;

//import kr.sols.notification.service.RedisMessageService;
import jakarta.servlet.http.HttpServletResponse;
import kr.sols.auth.dto.CheckTokenResponse;
import kr.sols.auth.dto.KakaoLoginRequest;
import kr.sols.auth.dto.TokenResponse;
import kr.sols.auth.service.AuthService;
import kr.sols.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AuthController {

    // TokenService 빈 주입
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping("/auth/kakao")
    public ResponseEntity<TokenResponse> kakaoLogin(@RequestBody KakaoLoginRequest request) {
        return ResponseEntity.ok(authService.loginWithKakao(request));
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        // 리프레시 토큰 삭제
        tokenService.deleteRefreshToken(userDetails.getUsername());

        // 로그아웃 성공 응답 반환 (204 No Content)
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/check")
    public ResponseEntity<CheckTokenResponse> checkTokenAndRefresh(@RequestHeader("Authorization") String accessToken, HttpServletResponse response) {
        // "Bearer " 접두사 제거
        String token = accessToken.replace("Bearer ", "");

        // 토큰 값으로 서비스 호출
        return ResponseEntity.ok(authService.checkTokenAndRefresh(token, response));
    }
}