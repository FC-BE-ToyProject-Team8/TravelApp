package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class InvalidDateSequenceException extends BaseException {

    public InvalidDateSequenceException() {
        super(ErrorCode.INVALID_DATE_SEQUENCE.getErrorMsg());
    }
}
