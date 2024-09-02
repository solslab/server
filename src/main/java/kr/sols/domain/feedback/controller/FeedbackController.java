package kr.sols.domain.feedback.controller;

import kr.sols.auth.annotation.RoleAdmin;
import kr.sols.domain.feedback.dto.FeedbackDto;
import kr.sols.domain.feedback.dto.FeedbackCreatedRes;
import kr.sols.domain.feedback.dto.FeedbackListDto;
import kr.sols.domain.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackCreatedRes> createFeedback(@RequestBody FeedbackDto feedbackDto) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackDto));
    }

    @RoleAdmin
    @GetMapping
    public ResponseEntity<FeedbackListDto> getAllFeedbacks() {
        FeedbackListDto feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }
}
