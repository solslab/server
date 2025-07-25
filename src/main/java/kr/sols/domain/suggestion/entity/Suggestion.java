package kr.sols.domain.suggestion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.sols.common.BaseTimeEntity;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.position.entity.Position;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {
        @Index(name = "idx_suggestion_created_date", columnList = "created_date")
})
public class Suggestion extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "member cannot be null")
    @JoinColumn(name = "member_key", referencedColumnName = "memberKey")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "position cannot be null")
    @JoinColumn(name = "position_id")
    private Position position;

    @NotEmpty(message = "정보수정요청 내용을 입력해주세요")
    @Column(length = 200)
    private String suggestionContent;

    @Enumerated(value = EnumType.STRING)
    private Status suggestionStatus;

    @Builder
    public Suggestion(Member member, Position position, String suggestionContent, Status suggestionStatus) {
        this.member = member;
        this.position = position;
        this.suggestionContent = suggestionContent;
        this.suggestionStatus = suggestionStatus;
    }

    public void updateStatus(Status status) {
        if (status != null) {
            this.suggestionStatus = status;
        }
    }
}
