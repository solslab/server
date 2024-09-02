package kr.sols.domain.feedback.service;

import kr.sols.domain.feedback.dto.FeedbackCreatedRes;
import kr.sols.domain.feedback.dto.FeedbackDto;
import kr.sols.domain.feedback.dto.FeedbackListDto;
import kr.sols.domain.feedback.entity.Feedback;
import kr.sols.domain.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackCreatedRes createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback(feedbackDto.getRating(), feedbackDto.getFeedbackContent());
        feedback = feedbackRepository.save(feedback);
        return new FeedbackCreatedRes(feedback.getId());
    }

    public FeedbackListDto getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAllByOrderByCreatedDateDesc();

        // 평균 레이팅 계산
        double averageRating = feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);

        List<FeedbackDto> feedbackList = feedbacks.stream()
                .map(FeedbackDto::fromEntity)
                .collect(Collectors.toList());

        // FeedbackListDto 생성 및 반환
        return new FeedbackListDto(averageRating, feedbackList);
    }
}
