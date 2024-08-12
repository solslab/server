package kr.sols.domain.admin.service;

import kr.sols.auth.jwt.TokenProvider;
import kr.sols.domain.admin.entity.Admin;
import kr.sols.domain.admin.exception.AdminException;
import kr.sols.domain.admin.repository.AdminRepository;
import kr.sols.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // Bcrypt 패스워드 인코더
    private final TokenProvider tokenProvider;

    public Admin createAdmin(String email, String password) {
        // 이메일 중복 체크
        adminRepository.findByEmail(email).ifPresent(a -> {
            throw new AdminException(DUPLICATED_ADMIN_NAME);
        });

        // 패스워드 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 어드민 엔티티 생성
        Admin admin = Admin.builder()
                .email(email)
                .password(encodedPassword)
                .role(Role.ADMIN) // 어드민 권한 부여
                .build();

        // DB에 저장
        return adminRepository.save(admin);
    }

    public String login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminException(ADMIN_PW_INCORRECT);
        }

        Authentication authentication = tokenProvider.getAuthentication(email);
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication, accessToken);

        return accessToken;
    }
}
