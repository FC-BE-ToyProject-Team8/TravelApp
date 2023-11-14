package kr.co.fastcampus.travel.common.exception;


import kr.co.fastcampus.travel.common.response.ErrorCode;

public class InvalidArgumentException extends BaseException {

    public InvalidArgumentException() {
        super(ErrorCode.COMMON_INVALID_ARGUMENT.getErrorMsg());
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
