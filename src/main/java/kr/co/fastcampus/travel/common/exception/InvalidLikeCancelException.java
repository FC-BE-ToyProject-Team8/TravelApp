package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class InvalidLikeCancelException extends BaseException {
    public InvalidLikeCancelException() {
        super(ErrorCode.INVALID_LIKE_CANCEL.getErrorMsg());
    }
}
