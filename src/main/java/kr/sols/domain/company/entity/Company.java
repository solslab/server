package kr.sols.domain.company.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.sols.common.StringListConverter;
import kr.sols.common.BaseTimeEntity;
import kr.sols.common.TypeValidator;
import kr.sols.domain.position.entity.Position;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Company extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank(message = "회사명을 입력해주세요")
    private String companyName;

    @Convert(converter = StringListConverter.class)
//    @NotEmpty(message = "산업분야를 입력해주세요")
    private List<String> industryType;

    private String companyLogo;

    @OneToMany(mappedBy = "company")
    private List<Position> positions;

    @Convert(converter = StringListConverter.class)
    private List<String> searchTerms;

    private boolean isPublic;

    @Builder
    public Company(String companyName, List<String> industryType, String companyLogo, List<String> searchTerms, boolean isPublic) {
        this.companyName = companyName;
        this.industryType = industryType;
        this.companyLogo = companyLogo;
        this.searchTerms = searchTerms;
        this.isPublic = isPublic;
    }

    public void updatePublicStatus(long reviewCount, long infoCount) {
        this.isPublic = reviewCount > 0 || infoCount > 0;
    }
}