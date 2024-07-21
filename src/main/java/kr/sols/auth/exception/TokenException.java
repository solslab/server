package kr.sols.auth.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}