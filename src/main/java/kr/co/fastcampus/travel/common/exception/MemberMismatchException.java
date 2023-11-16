package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class MemberMismatchException extends BaseException {
    public MemberMismatchException() {
        super(ErrorCode.MEMBER_MISMATCH.getErrorMsg());
    }
}
