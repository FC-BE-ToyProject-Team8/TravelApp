package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class ApiRequestFailedException extends BaseException {

    public ApiRequestFailedException() {
        super(ErrorCode.API_REQUEST_FAILED.getErrorMsg());
    }
}
