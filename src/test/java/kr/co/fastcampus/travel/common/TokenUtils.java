package kr.co.fastcampus.travel.common;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.MemberTestUtils.PASSWORD;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;

import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.secure.controller.dto.request.LoginReqeust;

public final class TokenUtils {

    public static final String GRANT_TYPE = "Bearer ";

    private TokenUtils() {
    }

    public static String getAccessToken() {
        createMember();
        String url = "/api/login";
        LoginReqeust request = new LoginReqeust(EMAIL, PASSWORD);
        return GRANT_TYPE + restAssuredPostBody(url, request)
            .jsonPath()
            .getString("data.accessToken");
    }

    public static String getAccessToken(LoginReqeust request) {
        String url = "/api/login";
        return GRANT_TYPE + restAssuredPostBody(url, request)
                .jsonPath()
                .getString("data.accessToken");
    }

    private static void createMember() {
        String url = "/api/signup";
        MemberSaveRequest request = createMemberSaveRequest();
        restAssuredPostBody(url, request);
    }
}
