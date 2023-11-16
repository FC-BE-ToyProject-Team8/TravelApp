package kr.co.fastcampus.travel.domain.comment.controller;

import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPutWithToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentUpdateRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentSaveRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentUpdateRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentResponse;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CommentControllerTest extends ApiTest {

    @Test
    @DisplayName("댓글달기")
    void save() {
        // given
        TripSaveRequest tripSaveRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        restAssuredPostWithToken(API_TRIPS_ENDPOINT, tripSaveRequest);

        String url = "/api/comments?tripId=1";
        CommentSaveRequest request = createCommentSaveRequest();

        // when
        ExtractableResponse<Response> response = restAssuredPostWithToken(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        CommentResponse data = jsonPath.getObject("data", CommentResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.content()).isEqualTo(request.content());
        });
    }

    @Test
    @DisplayName("댓글 수정")
    void update() {
        // given
        TripSaveRequest tripSaveRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        String commentSaveUrl = "/api/comments?tripId=1";
        CommentSaveRequest commentSaveRequest = createCommentSaveRequest();

        restAssuredPostWithToken(API_TRIPS_ENDPOINT, tripSaveRequest);
        restAssuredPostWithToken(commentSaveUrl, commentSaveRequest);

        String url = "/api/comments/1";
        CommentUpdateRequest request = createCommentUpdateRequest();

        // when
        ExtractableResponse<Response> response = restAssuredPutWithToken(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        CommentResponse data = jsonPath.getObject("data", CommentResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.content()).isEqualTo(request.content());
        });
    }

    @Test
    @DisplayName("댓글 삭제")
    void delete() {
        // given
        TripSaveRequest tripSaveRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        String commentSaveUrl = "/api/comments?tripId=1";
        CommentSaveRequest commentSaveRequest = createCommentSaveRequest();

        restAssuredPostWithToken(API_TRIPS_ENDPOINT, tripSaveRequest);
        restAssuredPostWithToken(commentSaveUrl, commentSaveRequest);

        String url = "/api/comments/1";

        // when
        ExtractableResponse<Response> response = restAssuredDeleteWithToken(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("status")).isEqualTo("SUCCESS");
    }
}
