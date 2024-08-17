package kr.sols.domain.testReview.dto;

import kr.sols.domain.position.dto.PositionListDto;
import kr.sols.domain.position.entity.Position;
import kr.sols.domain.testReview.entity.TestReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.sols.common.FormatDate.formatDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestReviewListDto {
    private UUID trId;
    private String memberName;
    private String companyName;
    private String createdDate;

    public static TestReviewListDto fromEntity(TestReview testReview) {
        return TestReviewListDto.builder()
                .trId(testReview.getId())
                .memberName(testReview.getMemberName())
                .companyName(testReview.getCompanyName())
                .createdDate(formatDate(testReview.getCreatedDate()))
                .build();
    }
}
