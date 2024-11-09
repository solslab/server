package kr.sols.domain.testReview.dto;

import kr.sols.domain.testReview.entity.TestReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static kr.sols.common.FormatDate.formatDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewDto {
    private String companyName;
    private String memberName;
    private int memberTier;
    private String trYear;
    private String trPosition;
    private String trCareer;
    private int trProblemNum;
    private float trSolvedNum;
    private String trPassStatus;
    private List<String> trProblemType;
    private String trComment;
    private String createdDate;

    public static TestReviewDto fromEntity(TestReview testReview) {
        return TestReviewDto.builder()
                .companyName(testReview.getCompanyName())
                .memberName(testReview.getMemberName())
                .memberTier(testReview.getMemberTier())
                .trYear(testReview.getTrYear())
                .trPosition(testReview.getTrPosition())
                .trCareer(testReview.getTrCareer())
                .trProblemNum(testReview.getTrProblemNum())
                .trSolvedNum(testReview.getTrSolvedNum())
                .trPassStatus(testReview.getTrPassStatus())
                .trProblemType(testReview.getTrProblemType())
                .trComment(testReview.getTrComment())
                .createdDate(formatDate(testReview.getCreatedDate()))
                .build();
    }
}
