package kr.sols.domain.testReview.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import kr.sols.common.BaseTimeEntity;
import kr.sols.common.StringListConverter;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TestReview extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String memberKey; // 반정규화

    @Column(length = 5)
    private String memberName; // 반정규화
    private UUID companyId; // 반정규화

    @Column(length = 20)
    @NotBlank(message = "회사명을 입력해주세요")
    private String companyName; // 반정규화

    @NotNull(message = "회원 티어를 입력해주세요")
    @Min(0)
    @Max(36)
    private Integer memberTier; // 반정규화

    @NotNull(message = "응시년도를 입력해주세요")
    private String trYear;

    @Column(length = 15, nullable = false)
    @NotBlank(message = "직무를 입력해주세요")
    private String trPosition;

    @NotBlank(message = "경력구분을 선택해주세요")
    @Column(length = 2)
    @Pattern(regexp = "^(신입|경력)$")
    private String trCareer;

    @NotNull(message = "총 문제 수를 입력해주세요")
    @Min(1)
    @Max(30)
    private Integer trProblemNum;

    @NotNull(message = "푼 문제 수를 입력해주세요")
    @DecimalMin(value = "0.0", message = "푼 문제 수는 0 이상이어야 합니다")
    @DecimalMax(value = "30.0", message = "푼 문제 수는 30 이하이어야 합니다")
    private float trSolvedNum;

    @NotBlank(message = "합격여부를 선택해주세요")
    @Column(length = 3, nullable = false)
    @Pattern(regexp = "^(합격|불합격)$")
    private String trPassStatus;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    @NotEmpty(message = "문제유형을 선택해주세요")
    private List<String> trProblemType;

    @Column(nullable = false)
    @Min(value = 1, message = "난이도는 최소 1 이상이어야 합니다")
    @Max(value = 5, message = "난이도는 최대 5까지만 입력 가능합니다")
    @NotNull(message = "난이도를 입력해주세요")
    private Integer difficulty;

    @NotBlank(message = "한줄후기를 입력해주세요")
    @Column(length = 100, nullable = false)
    private String trComment;


    @Builder
    public TestReview(UUID id, String memberKey, String memberName, UUID companyId, String companyName, Integer memberTier, String trYear, String trPosition, String trCareer, Integer trProblemNum, float trSolvedNum, String trPassStatus, List<String> trProblemType, Integer difficulty, String trComment) {
        this.id = id;
        this.memberKey = memberKey;
        this.memberName = memberName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.memberTier = memberTier;
        this.trYear = trYear;
        this.trPosition = trPosition;
        this.trCareer = trCareer;
        this.trProblemNum = trProblemNum;
        this.trSolvedNum = trSolvedNum;
        this.trPassStatus = trPassStatus;
        this.trProblemType = trProblemType;
        this.difficulty = difficulty;
        this.trComment = trComment;
    }
}
