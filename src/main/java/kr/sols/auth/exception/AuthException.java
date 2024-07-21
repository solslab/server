package kr.sols.auth.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}