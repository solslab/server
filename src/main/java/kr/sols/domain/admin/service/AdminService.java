package kr.sols.domain.admin.service;

import kr.sols.auth.jwt.TokenProvider;
import kr.sols.domain.admin.entity.Admin;
import kr.sols.domain.admin.exception.AdminException;
import kr.sols.domain.admin.repository.AdminRepository;
import kr.sols.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

import java.util.Collections;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // Bcrypt 패스워드 인코더
    private final TokenProvider tokenProvider;

    public Admin createAdmin(String email, String password) {
        adminRepository.findByEmail(email).ifPresent(a -> {
            throw new AdminException(DUPLICATED_ADMIN_NAME);
        });

        String encodedPassword = passwordEncoder.encode(password);

        Admin admin = Admin.builder()
                .email(email)
                .password(encodedPassword)
                .role(Role.ADMIN)
                .build();

        return adminRepository.save(admin);
    }

    public String login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new AdminException(ADMIN_PW_INCORRECT);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                admin.getEmail(), // 사용자 이름
                null, // 비밀번호는 필요 없으므로 null
                Collections.singletonList(new SimpleGrantedAuthority(admin.getRole().getKey()))
        );

        // 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication, accessToken);

        return accessToken;
    }
}
