package kr.sols.domain.company.entity;

import jakarta.persistence.*;
import kr.sols.common.StringListConverter;
import kr.sols.common.BaseTimeEntity;
import kr.sols.common.TypeValidator;
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

    @Column(nullable = false, length = 10)
    private String companyName;

    @Convert(converter = StringListConverter.class)
    @Column(nullable = false)
    private List<String> industryType;

    private String companyLogo;

    @Builder
    public Company(String companyName, List<String> industryType, String companyLogo) {
        this.companyName = companyName;
        this.industryType = industryType;
        this.companyLogo = companyLogo;
    }

//    public void updateCompany(MemberEditRequest request) {
//        if (request.get != null) {
//            this.preferLanguages = request.getPreferLanguages();
//        }
//    }
}