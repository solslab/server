package kr.sols.domain.admin.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.service.TokenService;
import kr.sols.domain.admin.dto.AdminDto;
import kr.sols.domain.admin.dto.LoginResponse;
import kr.sols.domain.admin.entity.Admin;
import kr.sols.domain.admin.exception.AdminException;
import kr.sols.domain.admin.repository.AdminRepository;
import kr.sols.domain.admin.service.AdminService;
import kr.sols.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static kr.sols.exception.ErrorCode.ADMIN_NOT_FOUND;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final TokenService tokenService;
    private final AdminRepository adminRepository;

    @PostMapping()
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminDto request) {
        Admin admin = adminService.createAdmin(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminDto request) {
        String accessToken = adminService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("accessToken", accessToken));
    }

    @RoleAdmin
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String email) {
        // 유효한 이메일인지 확인
        adminRepository.findByEmail(email).orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));

        // 리프레시 토큰 삭제
        tokenService.deleteRefreshToken(email);

        // 로그아웃 성공 응답 반환 (204 No Content)
        return ResponseEntity.noContent().build();
    }

}

