package kr.sols.domain.company.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class CompanyException extends CustomException {
    public CompanyException(ErrorCode errorCode) { super(errorCode);}
}
