package kr.co.fastcampus.travel.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_INVALID_ARGUMENT("잘못된 요청입니다."),
    TOKEN_EXPIRED("만료된 토큰입니다."),
    COMMENT_MEMBER_MISMATCH("해당 댓글에 수정 권한이 없습니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
