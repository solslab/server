package kr.sols.domain.position.Exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class PositionException extends CustomException {
    public PositionException(ErrorCode errorCode) { super(errorCode);}
}
