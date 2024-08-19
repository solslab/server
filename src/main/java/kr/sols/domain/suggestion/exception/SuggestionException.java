package kr.sols.domain.suggestion.exception;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class SuggestionException extends CustomException {
    public SuggestionException(ErrorCode errorCode) { super(errorCode);}
}
