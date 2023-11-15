package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class MemberMissMatchException extends BaseException {

    public MemberMissMatchException() {
        super(ErrorCode.COMMENT_MEMBER_MISMATCH.getErrorMsg());
    }
}
