package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class MemberNotFoundException extends BaseException{

    public MemberNotFoundException() { super(ErrorCode.MEMBER_NOT_FOUND.getErrorMsg()); }
}
