package kr.sols.domain.testReview.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.auth.exception.TokenException;
import kr.sols.domain.member.exception.MemberException;
import kr.sols.domain.position.dto.PositionCreatedResponse;
import kr.sols.domain.position.dto.PositionDto;
import kr.sols.domain.position.service.PositionService;
import kr.sols.domain.testReview.dto.*;
import kr.sols.domain.testReview.exception.TestReviewException;
import kr.sols.domain.testReview.service.TestReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kr.sols.exception.ErrorCode.NO_ACCESS;

@RestController
@RequiredArgsConstructor
public class TestReviewController {
    private final TestReviewService testReviewService;

    // 코테 후기 생성
    @RoleUser
    @PostMapping("/tr")
    public ResponseEntity<TestReviewCreatedResponse> createTestReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TestReviewRequest request
    ) {
        TestReviewCreatedResponse res = testReviewService.createTestReview(request, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    // 코테 후기 전체 조회
    @RoleAdmin
    @GetMapping("/tr")
    public ResponseEntity<TestReviewPageDto> getAllTestReviews(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(testReviewService.getAllTestReviews(pageNum, pageSize));
    }

    // 코테 후기 상세 조회
    @RoleAdmin
    @GetMapping("/tr/{trId}")
    public ResponseEntity<TestReviewDto> getTestReview(
            @PathVariable UUID trId
    ){
        TestReviewDto res = testReviewService.getTestReview(trId);
        return ResponseEntity.ok(res);
    }

    // 데이터랩: 기업별 코테 후기 리스트 조회
    @GetMapping("/tab/datalab/{companyId}")
    public List<TestReivewDataLabDto> getTestReviewsByCompanyId(
            @PathVariable UUID companyId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return testReviewService.getTestReviewsByCompanyId(companyId, authentication);
    }
}
