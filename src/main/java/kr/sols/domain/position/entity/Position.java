package kr.sols.domain.position.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import kr.sols.common.BaseTimeEntity;
import kr.sols.common.StringListConverter;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.member.dto.MemberEditRequest;
import kr.sols.domain.position.dto.PositionDto;
import kr.sols.domain.position.dto.PositionReq;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Position extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Company cannot be null")
    @JoinColumn(name = "company_id")
    private Company company;

    @NotBlank(message = "Position name cannot be blank")
    private String positionName;

    @Convert(converter = StringListConverter.class)
    @NotEmpty(message = "Support languages cannot be empty")
    private List<String> supportLanguages;

    private String testTime;
    private String problemInfo;

    @Column(length = 5)
    @Pattern(regexp = "^(가능|불가능)$")
    private String permitIde;

    @Column(length = 5)
    @Pattern(regexp = "^(가능|불가능)$")
    private String permitSearch;

    @Column(length = 5)
    @Pattern(regexp = "^(있음|없음)$")
    private String hiddenCase;

    @Column(length = 5)
    @Pattern(regexp = "^(대면|비대면)$")
    private String examMode;

    private String testPlace;

    @Column(length = 50)
    private String note;

    @Builder
    public Position(Company company, String positionName, List<String> supportLanguages,
                    String testTime, String problemInfo, String permitIde, String permitSearch, String hiddenCase,
                    String examMode, String testPlace, String note) {
        this.company = company;
        this.positionName = positionName;
        this.supportLanguages = supportLanguages;
        this.testTime = testTime;
        this.problemInfo = problemInfo;
        this.permitIde = permitIde;
        this.permitSearch = permitSearch;
        this.hiddenCase = hiddenCase;
        this.examMode = examMode;
        this.testPlace = testPlace;
        this.note = note;
    }

    public void updatePosition(PositionReq request) {
        if (request.getPositionName() != null) {
            this.positionName = request.getPositionName();
        }
        if (request.getSupportLanguages() != null) {
            this.supportLanguages = request.getSupportLanguages();
        }

        this.testTime = request.getTestTime();
        this.problemInfo = request.getProblemInfo();
        this.permitIde = request.getPermitIde();
        this.permitSearch = request.getPermitSearch();
        this.hiddenCase = request.getHiddenCase();
        this.examMode = request.getExamMode();
        this.testPlace = request.getTestPlace();
        this.note = request.getNote();
    }
}