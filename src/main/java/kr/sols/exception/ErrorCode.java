package kr.sols.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "illegal registration id"),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),

    // admin
    ADMIN_NOT_FOUND(NOT_FOUND, "일치하는 관리자 계정이 없습니다."),
    ADMIN_PW_INCORRECT(UNAUTHORIZED,"관리자 비밀번호가 일치하지 않습니다."),
    DUPLICATED_ADMIN_NAME(CONFLICT, "이미 등록된 이메일입니다."),

    // member
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),

    // company
    COMPANY_NOT_FOUND(NOT_FOUND, "기업을 찾을 수 없습니다."),
    DUPLICATED_COMPANY_NAME(CONFLICT, "이미 등록된 기업명입니다."),
    INVALID_INDUSTRY_TYPE(BAD_REQUEST, "등록되지 않은 Industry 값입니다."),

    // position
    POSITION_NOT_FOUND(NOT_FOUND, "직무를 찾을 수 없습니다."),
    DUPLICATED_POSITION_NAME(CONFLICT, "이미 등록된 직무명입니다."),
    INVALID_LANGUAGE_TYPE(BAD_REQUEST, "등록되지 않은 프로그래밍 언어 값입니다."),

    // test_review
    TEST_REVIEW_NOT_FOUND(NOT_FOUND, "코테 후기를 찾을 수 없습니다."),
    MEMBER_TIER_UNDEFINED(BAD_REQUEST, "회원의 티어 설정이 되지 않았습니다."),
    TR_COMPANY_NOT_MATCH(BAD_REQUEST, "기업 아이디와 기업명이 일치하지 않습니다."),

    // suggestion
    SUGGESTION_NOT_FOUND(NOT_FOUND, "정보수정요청을 찾을 수 없습니다."),

    // feedback
    FEEDBACK_NOT_FOUND(NOT_FOUND, "피드백을 찾을 수 없습니다."),

    // S3
    FAIL_TO_CONVERT_FILE(BAD_REQUEST, "MultipartFile을 File로 전환하는데 실패했습니다."),

    // global
    RESOURCE_LOCKED(LOCKED, "자원이 잠겨 있어 접근할 수 없습니다."),
    NO_ACCESS(FORBIDDEN, "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(NOT_FOUND, "요청한 자원을 찾을 수 없습니다."),
    INVALID_REQUEST(BAD_REQUEST, "올바르지 않은 요청입니다."),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 에러가 발생했습니다.");


    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}