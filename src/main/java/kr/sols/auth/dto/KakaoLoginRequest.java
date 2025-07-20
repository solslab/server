package kr.sols.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginRequest {
    private String code;
    @JsonProperty("redirectUri")
    private String redirectUri;
}
