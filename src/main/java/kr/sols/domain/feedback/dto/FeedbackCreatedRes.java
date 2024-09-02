package kr.sols.domain.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackCreatedRes {
    private UUID feedbackId;
}
