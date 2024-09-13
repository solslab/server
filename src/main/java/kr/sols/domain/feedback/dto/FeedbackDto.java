package kr.sols.domain.feedback.dto;

import kr.sols.common.FormatDate;
import kr.sols.domain.feedback.entity.Feedback;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class FeedbackDto {
    private int rating;
    private String feedbackContent;
    private String createdDate;

    public static FeedbackDto fromEntity(Feedback feedback) {
        return FeedbackDto.builder()
                .rating(feedback.getRating())
                .feedbackContent(feedback.getFeedbackContent())
                .createdDate(FormatDate.formatDate(feedback.getCreatedDate()))
                .build();
    }

}
