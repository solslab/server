package kr.sols.domain.admin.entity;

import jakarta.persistence.*;
import kr.sols.common.BaseTimeEntity;
import kr.sols.domain.member.entity.Role;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Admin extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;


    @Builder
    public Admin(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
