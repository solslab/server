package kr.sols.domain.position.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.position.dto.PositionCreatedResponse;
import kr.sols.domain.position.dto.PositionDto;
import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.position.dto.PositionTabDto;
import kr.sols.domain.position.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
//    @GetMapping("/company/{companyId}/position")
//    public List<PositionListDto> getAllPositionOfCompany(
//            @PathVariable UUID companyId
//    ) {
//        return positionService.getAllPositionOfCompany(companyId);
//    }

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

    // 기업 탭 - 직무별 코테정보 조회
    @GetMapping("tab/testinfo/{positionId}")
    public ResponseEntity<PositionTabDto> getPositionInfo(@PathVariable("positionId") UUID positionId) {
        // 인증 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isMember = authentication != null && authentication.isAuthenticated();

        PositionDto positionDto = positionService.getPosition(positionId);
        PositionTabDto positionTabDto = PositionTabDto.fromDto(positionDto, isMember);

        if (!isMember) {
            positionTabDto.setSupportLanguages(new ArrayList<>()); // 비회원인 경우 지원언어 숨기기
        }

        return ResponseEntity.ok(positionTabDto);
    }
}
