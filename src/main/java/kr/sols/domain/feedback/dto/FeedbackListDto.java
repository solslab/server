package kr.sols.domain.feedback.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedbackListDto {
    private double averageRating;
    private List<FeedbackDto> feedbackList;

    public FeedbackListDto(double averageRating, List<FeedbackDto> feedbackList) {
        this.averageRating = Math.round(averageRating * 10.0) / 10.0;
        this.feedbackList = feedbackList;
    }

}
