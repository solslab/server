package kr.sols.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import kr.sols.auth.dto.CheckTokenResponse;
import kr.sols.auth.exception.TokenException;
import kr.sols.auth.jwt.TokenProvider;
import kr.sols.exception.ErrorCode;
import kr.sols.redis.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    @Transactional
    public CheckTokenResponse checkTokenAndRefresh(String accessToken, HttpServletResponse response) {
        if (tokenProvider.validateToken(accessToken)) {  // 액세스 토큰 유효
            return new CheckTokenResponse("validate");
        }
        else { // 액세스 토큰 만료됨
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            String refreshToken = token.getRefreshToken();

            if (tokenProvider.validateToken(refreshToken)) { // 리프레쉬 유효함
                String reissueAccessToken = tokenProvider.generateAccessToken(tokenProvider.getAuthentication(refreshToken));
                tokenService.updateToken(reissueAccessToken, token); // redis에 업데이트
                // 새롭게 발급된 토큰을 response 헤더에 설정
                if (reissueAccessToken != null && !reissueAccessToken.isEmpty()) {
                    response.setHeader("Authorization", "Bearer " + reissueAccessToken);
                }
                return new CheckTokenResponse("invalidate");
            }
            else throw new TokenException(ErrorCode.TOKEN_EXPIRED);
        }
    }
}
