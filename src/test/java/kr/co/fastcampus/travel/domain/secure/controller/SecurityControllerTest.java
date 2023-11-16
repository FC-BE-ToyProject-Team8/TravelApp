package kr.co.fastcampus.travel.domain.secure.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.MemberTestUtils.PASSWORD;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createLoginRequest;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveDto;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.secure.controller.dto.request.LoginReqeust;
import kr.co.fastcampus.travel.domain.secure.controller.dto.request.ReissueRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class SecurityControllerTest extends ApiTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인")
    void login() {
        // given
        memberService.save(createMemberSaveDto());

        String url = "/api/login";
        LoginReqeust request = new LoginReqeust(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("data.grantType")).isNotNull();
        assertThat(jsonPath.getString("data.accessToken")).isNotNull();
        assertThat(jsonPath.getString("data.refreshToken")).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인")
    void notExistMember() {
        // given
        memberService.save(createMemberSaveDto());

        String url = "/api/login";
        LoginReqeust request = new LoginReqeust("otherEmail", PASSWORD);

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("잘못된 비밀번호 로그인")
    void notMatchMember() {
        // given
        memberService.save(createMemberSaveDto());

        String url = "/api/login";
        LoginReqeust request = new LoginReqeust(EMAIL, "otherPassword");

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("토큰 재발급")
    void reissueToken() {
        // given
        memberService.save(createMemberSaveDto());
        String refreshToken = restAssuredPostBody("/api/login", createLoginRequest())
                .jsonPath().getString("data.refreshToken");

        String url = "/api/reissue";
        ReissueRequest request = new ReissueRequest(EMAIL, refreshToken);

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("data.grantType")).isNotNull();
        assertThat(jsonPath.getString("data.accessToken")).isNotNull();
        assertThat(jsonPath.getString("data.refreshToken")).isNotNull();
    }
}
