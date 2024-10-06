package kr.sols.domain.feedback.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackPageDto {
    private List<FeedbackDto> feedbacks;
    private int totalElements; // 총 멤버 수
    private int totalPages; // 총 페이지 수
    private int currentPage; // 현재 페이지 번호
    private int pageSize; // 페이지당 데이터 수
}
