package kr.co.fastcampus.travel.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    DUPLICATED_LIKE("중복된 좋아요입니다."),
    MEMBER_NOT_FOUND("존재하지 않는 사용자입니다.");


    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }
}
