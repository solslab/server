package kr.sols.domain.feedback.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class FeedbackException extends CustomException {
    public FeedbackException(ErrorCode errorCode) {super(errorCode);}
}
