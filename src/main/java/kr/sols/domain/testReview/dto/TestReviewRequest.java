package kr.sols.domain.testReview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewRequest {
    private UUID companyId;
    private String companyName;
    private String trYear;
    private String trPostion;
    private String trCareer;
    private int trProblemNum;
    private float trSolvedNum;
    private String trPassStatus;
    private List<String> trProblemType;
    private String trComment;
}
