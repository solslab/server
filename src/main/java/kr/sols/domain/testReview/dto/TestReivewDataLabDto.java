package kr.sols.domain.testReview.dto;

import kr.sols.domain.testReview.entity.TestReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReivewDataLabDto {
    private int memberTier;
    private String trYear;
    private String trPosition;
    private String trCareer;
    private int trProblemNum;
    private int trSolvedNum;
    private String trPassStatus;

    public static TestReivewDataLabDto fromEntity(TestReview testReview) {
        return TestReivewDataLabDto.builder()
                .memberTier(testReview.getMemberTier())
                .trYear(testReview.getTrYear())
                .trPosition(testReview.getTrPosition())
                .trCareer(testReview.getTrCareer())
                .trProblemNum(testReview.getTrProblemNum())
                .trSolvedNum(testReview.getTrSolvedNum())
                .trPassStatus(testReview.getTrPassStatus())
                .build();
    }
}
