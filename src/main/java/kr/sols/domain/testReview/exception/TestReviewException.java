package kr.sols.domain.testReview.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class TestReviewException extends CustomException {
    public TestReviewException(ErrorCode errorCode) { super(errorCode);}
}
