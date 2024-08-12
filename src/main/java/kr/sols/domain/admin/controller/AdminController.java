package kr.sols.domain.admin.controller;

import kr.sols.domain.admin.dto.AdminDto;
import kr.sols.domain.admin.dto.LoginResponse;
import kr.sols.domain.admin.entity.Admin;
import kr.sols.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @PostMapping()
    public ResponseEntity<Admin> createAdmin(@RequestBody AdminDto request) {
        Admin admin = adminService.createAdmin(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AdminDto request) {
        String accessToken = adminService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(accessToken));
    }
}

