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
public class TestReviewRequest {
    private int trYear;
    private String trPostion;
    private String trCareer;
    private int trProblemNum;
    private int trSolvedNum;
    private String trPassStatus;
    private List<String> trProblemType;
    private String trComment;
}
