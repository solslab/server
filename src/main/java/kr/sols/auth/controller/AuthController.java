package kr.sols.auth.controller;

//import kr.sols.notification.service.RedisMessageService;
import kr.sols.auth.dto.LoginResponse;
import kr.sols.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    // TokenService 빈 주입
    private final TokenService tokenService;

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
}