package kr.sols.domain.testReview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewPageDto {
    private List<TestReviewListDto> testReviews;
    private int totalElements; // 총 리뷰 수
    private int totalPages; // 총 페이지 수
    private int currentPage; // 현재 페이지 번호
    private int pageSize; // 페이지당 데이터 수
}
