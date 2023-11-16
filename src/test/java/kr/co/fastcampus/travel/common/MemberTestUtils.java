package kr.co.fastcampus.travel.common;

import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.dto.request.MemberSaveDto;

public final class MemberTestUtils {

    public static final String EMAIL = "email@email.com";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";

    private MemberTestUtils() {
    }

    public static Member createMember() {
        return Member.builder()
            .email(EMAIL)
            .name(NAME)
            .nickname(NICKNAME)
            .password(PASSWORD)
            .build();
    }

    public static MemberSaveRequest createMemberSaveReqeust() {
        return new MemberSaveRequest(
            EMAIL,
            NAME,
            NICKNAME,
            PASSWORD
        );
    }

    public static MemberSaveDto createMemberSaveDto() {
        return new MemberSaveDto(EMAIL, NAME, NICKNAME, PASSWORD);
    }
}
