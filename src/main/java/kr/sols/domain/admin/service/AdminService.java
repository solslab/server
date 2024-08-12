package kr.sols.domain.admin.service;

import kr.sols.domain.admin.entity.Admin;
import kr.sols.domain.admin.exception.AdminException;
import kr.sols.domain.admin.repository.AdminRepository;
import kr.sols.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static kr.sols.exception.ErrorCode.DUPLICATED_ADMIN_NAME;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // Bcrypt 패스워드 인코더

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
}
