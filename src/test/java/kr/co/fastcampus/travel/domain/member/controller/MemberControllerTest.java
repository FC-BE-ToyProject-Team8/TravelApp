package kr.co.fastcampus.travel.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MemberControllerTest extends ApiTest {

    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    void save() {
        // given
        String url = "/signup";
        MemberSaveRequest request = createMemberSaveReqeust();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getLong("data.id")).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원가입 이미 가입된 이메일")
    void alreadySignupEmail() {
        // given
        saveMember(createMember());

        String url = "/signup";
        MemberSaveRequest request = createMemberSaveReqeust();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("회원가입 이미 가입된 닉네임")
    void alreadySignupNickname() {
        // given
        saveMember(createMember());

        String url = "/signup";
        MemberSaveRequest request = new MemberSaveRequest(
                "otherEmail",
                NAME,
                NICKNAME,
                PASSWORD
        );

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Member createMember() {
        return Member.builder()
                .email(EMAIL)
                .name(NAME)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
    }

    private MemberSaveRequest createMemberSaveReqeust() {
        return new MemberSaveRequest(
                EMAIL,
                NAME,
                NICKNAME,
                PASSWORD
        );
    }

    private void saveMember(Member member) {
        memberRepository.save(member);
    }
}
