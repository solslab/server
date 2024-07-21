package kr.sols.dto.member;
import jakarta.validation.constraints.NotBlank;

public record MemberEditRequest(
        @NotBlank String name
) {
}