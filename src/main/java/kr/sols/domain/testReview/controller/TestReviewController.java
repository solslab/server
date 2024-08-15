package kr.sols.domain.testReview.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.position.dto.PositionCreatedResponse;
import kr.sols.domain.position.dto.PositionDto;
import kr.sols.domain.position.service.PositionService;
import kr.sols.domain.testReview.dto.TestReviewCreatedResponse;
import kr.sols.domain.testReview.dto.TestReviewDto;
import kr.sols.domain.testReview.dto.TestReviewRequest;
import kr.sols.domain.testReview.service.TestReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestReviewController {
    private final TestReviewService testReviewService;

    @RoleUser
    @PostMapping("/tr/{companyId}")
    public ResponseEntity<TestReviewCreatedResponse> createTestReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TestReviewRequest request,
            @PathVariable UUID companyId
    ) {
        TestReviewCreatedResponse res = testReviewService.createTestReview(request, companyId, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }


}
