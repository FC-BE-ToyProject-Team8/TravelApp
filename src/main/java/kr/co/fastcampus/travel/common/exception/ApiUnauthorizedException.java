package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class ApiUnauthorizedException extends BaseException {

    public ApiUnauthorizedException() {
        super(ErrorCode.API_UNAUTHORIZED.getErrorMsg());
    }
}
