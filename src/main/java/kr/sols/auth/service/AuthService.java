package kr.sols.auth.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import kr.sols.auth.dto.CheckTokenResponse;
import kr.sols.auth.dto.KakaoLoginRequest;
import kr.sols.auth.dto.KakaoUserResponse;
import kr.sols.auth.dto.TokenResponse;
import kr.sols.auth.exception.TokenException;
import kr.sols.auth.jwt.TokenProvider;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.member.entity.Role;
import kr.sols.domain.member.repository.MemberRepository;
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
    private final KakaoOAuthService kakaoOAuthService;
    private final MemberRepository memberRepository;

    @Transactional
    public CheckTokenResponse checkTokenAndRefresh(String accessToken, HttpServletResponse response) {

        if (tokenProvider.validateToken(accessToken)) {  // 액세스 토큰 유효
            return new CheckTokenResponse("validate", getRoleByToken(accessToken));
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
                return new CheckTokenResponse("invalidate", getRoleByToken(reissueAccessToken));
            }
            else throw new TokenException(ErrorCode.TOKEN_EXPIRED);
        }
    }

    public String getRoleByToken(String accessToken) {
        Claims claims = tokenProvider.parseClaims(accessToken);
        String role = claims.get("role", String.class);
        if (role != null && role.startsWith("ROLE_")) {
            role = role.replaceFirst("ROLE_", "");
        }
        return role;
    }


    public TokenResponse loginWithKakao(KakaoLoginRequest request) {
        String accessToken = kakaoOAuthService.getAccessToken(request.getCode(), request.getRedirectUri());
        KakaoUserResponse kakaoUser = kakaoOAuthService.getUserInfo(accessToken);

        Member member = memberRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(() -> memberRepository.save(kakaoUser.toMember()));

        String token = tokenProvider.generateAccessToken(member.getMemberKey(), member.getRole());
        return new TokenResponse(token);
    }

}
