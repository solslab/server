package kr.sols.auth.dto;

import kr.sols.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CheckTokenResponse {
    String AccessStatus;
    String role;
}
