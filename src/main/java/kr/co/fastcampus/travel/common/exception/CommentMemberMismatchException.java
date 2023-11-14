package kr.co.fastcampus.travel.common.exception;

import kr.co.fastcampus.travel.common.response.ErrorCode;

public class CommentMemberMismatchException extends BaseException{

    public CommentMemberMismatchException(){
        super(ErrorCode.COMMENT_MEMBER_MISMATCH.getErrorMsg());
    }
}
