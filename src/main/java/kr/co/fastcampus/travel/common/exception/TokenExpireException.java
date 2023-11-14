package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class TokenExpireException extends BaseException {

    public TokenExpireException() {
        super(ErrorCode.TOKEN_EXPIRED.getErrorMsg());
    }
}
