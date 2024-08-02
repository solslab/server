package kr.sols.member.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
