package kr.sols.domain.admin.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class AdminException extends CustomException {
    public AdminException(ErrorCode errorCode) {
        super(errorCode);
    }
}
