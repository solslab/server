package kr.sols.auth.service;

import kr.sols.auth.dto.CheckTokenResponse;
import kr.sols.auth.jwt.TokenProvider;
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
    public CheckTokenResponse checkTokenAndRefresh(String accessToken) {
        if (tokenProvider.validateToken(accessToken)) {  // 액세스 토큰 유효
            return new CheckTokenResponse("validate", null, null);
        }
        else { // 액세스 토큰 만료됨
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            String refreshToken = token.getRefreshToken();

            if (tokenProvider.validateToken(refreshToken)) { // 리프레쉬 유효함
                String reissueAccessToken = tokenProvider.generateAccessToken(tokenProvider.getAuthentication(refreshToken));
                tokenService.updateToken(reissueAccessToken, token); // redis에 업데이트
                return new CheckTokenResponse("invalidate", "validate", reissueAccessToken);
            }
            else {
                return new CheckTokenResponse("invalidate", "invalidate", null);
            }
        }
    }
}
