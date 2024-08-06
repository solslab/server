package kr.sols.s3;

import kr.sols.exception.CustomException;
import kr.sols.exception.ErrorCode;

public class S3Exception extends CustomException {
    public S3Exception(ErrorCode errorCode) {
        super(errorCode);
    }
}