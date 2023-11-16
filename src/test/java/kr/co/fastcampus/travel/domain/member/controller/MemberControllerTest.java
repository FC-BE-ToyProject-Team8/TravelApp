package kr.co.fastcampus.travel.domain.member.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.NAME;
import static kr.co.fastcampus.travel.common.MemberTestUtils.NICKNAME;
import static kr.co.fastcampus.travel.common.MemberTestUtils.PASSWORD;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMember;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static org.assertj.core.api.Assertions.assertThat;

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

class MemberControllerTest extends ApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입")
    void save() {
        // given
        String url = "/api/signup";
        MemberSaveRequest request = createMemberSaveRequest();

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

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

        String url = "/api/signup";
        MemberSaveRequest request = createMemberSaveRequest();

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("회원가입 이미 가입된 닉네임")
    void alreadySignupNickname() {
        // given
        saveMember(createMember());

        String url = "/api/signup";
        MemberSaveRequest request = new MemberSaveRequest(
            "otherEmail",
            NAME,
            NICKNAME,
            PASSWORD
        );

        // when
        ExtractableResponse<Response> response = restAssuredPostBody(url, request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void saveMember(Member member) {
        memberRepository.save(member);
    }
}
