package kr.sols.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}