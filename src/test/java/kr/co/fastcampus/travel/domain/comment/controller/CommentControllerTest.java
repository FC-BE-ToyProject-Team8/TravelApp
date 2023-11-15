package kr.co.fastcampus.travel.domain.comment.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveReqeust;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentSaveRequest;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.common.TokenUtils;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentSaveRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentResponse;
import kr.co.fastcampus.travel.domain.comment.repository.CommentRepository;
import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class CommentControllerTest extends ApiTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("댓글달기")
    void save() {
        // given
        String url = "/api/comments";
        CommentSaveRequest request = createCommentSaveRequest();

        TripSaveRequest saveTrip = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        String memberSignUpUrl = "/signup";
        MemberSaveRequest saveMember = createMemberSaveReqeust();
        restAssuredPostBody(memberSignUpUrl, request);

        restAssuredPostWithToken(API_TRIPS_ENDPOINT, saveTrip);
        restAssuredPostWithToken(memberSignUpUrl, saveMember);

        // when
        ExtractableResponse<Response> response = restAssuredPostWithToken(url, request);

        //then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        CommentResponse data = jsonPath.getObject("data", CommentResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.content()).isEqualTo(request.content());
        });
    }

}
