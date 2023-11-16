package kr.co.fastcampus.travel.common.exception;


import kr.co.fastcampus.travel.common.response.ErrorCode;

public class InvalidAuthenticationException extends BaseException {

    public InvalidAuthenticationException() {
        super(ErrorCode.INVALID_AUTHENTICATION.getErrorMsg());
    }
}
