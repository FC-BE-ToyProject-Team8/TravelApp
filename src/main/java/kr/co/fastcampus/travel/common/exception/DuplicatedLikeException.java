package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class DuplicatedLikeException extends BaseException {

    public DuplicatedLikeException() {
        super(ErrorCode.DUPLICATED_LIKE.getErrorMsg());
    }
}
