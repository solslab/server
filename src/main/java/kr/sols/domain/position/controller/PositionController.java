package kr.sols.domain.position.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.position.dto.PositionCreatedResponse;
import kr.sols.domain.position.dto.PositionDto;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.position.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    // 직무 생성
    @RoleAdmin
    @PostMapping("/company/{companyId}/position")
    public ResponseEntity<PositionCreatedResponse> createPosition(
            @RequestBody PositionDto positionDto,
            @PathVariable UUID companyId
    ) {
        PositionCreatedResponse createdPosition = positionService.createPosition(companyId, positionDto);
        return ResponseEntity.ok(createdPosition);
    }

    // 특정 기업의 직무 조회
    @GetMapping("/company/{companyId}/position")
    public List<PositionListDto> getAllPositionOfCompany(
            @PathVariable UUID companyId
    ) {
        return positionService.getAllPositionOfCompany(companyId);
    }

    // 직무 상세조회
    @RoleAdmin
    @GetMapping("/position/{positionId}")
    public ResponseEntity<PositionDto> getPosition(@PathVariable UUID positionId) {
        PositionDto positionDto = positionService.getPosition(positionId);
        return ResponseEntity.ok(positionDto);
    }

    // 직무 수정
    @RoleAdmin
    @PutMapping("/position/{positionId}")
    public ResponseEntity<PositionDto> updatePosition(
            @PathVariable UUID positionId,
            @RequestBody PositionDto positionDto) {
        PositionDto updatedPosition = positionService.updatePosition(positionId, positionDto);
        return ResponseEntity.ok(updatedPosition);
    }

    // 직무 삭제
    @RoleAdmin
    @DeleteMapping("/position/{positionId}")
    public ResponseEntity<Void> deletePosition(@PathVariable("positionId") UUID positionId) {
        positionService.deletePosition(positionId);
        return ResponseEntity.noContent().build();
    }
//
//    @GetMapping("/position/info/{positionId}")
//    public ResponseEntity<PositionDto> getPositionInfo(@PathVariable("positionId") UUID positionId) {
//        PositionDto positionDto = positionService.getPosition(positionId);
//        return ResponseEntity.ok(positionDto);
//    }

    @GetMapping("position/info/{positionId}")
    public ResponseEntity<?> getPositionInfo(@PathVariable("positionId") UUID positionId) {
        // 현재 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않은 상태에서 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You need to be authenticated to access this resource.");
        }

        // 인증된 상태에서의 처리
        try {
            PositionDto positionDto = positionService.getPosition(positionId);
            return ResponseEntity.ok(positionDto);
        } catch (Exception e) {
            // 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }
}
