package kr.sols.member.dto;
import jakarta.validation.constraints.NotBlank;

public record MemberEditRequest(
        @NotBlank String name
) {
}