package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithTokenLogin;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGet;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithTokenLogin;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripSaveDto;
import static kr.co.fastcampus.travel.common.response.Status.FAIL;
import static kr.co.fastcampus.travel.common.response.Status.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class TripControllerGetTripTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        TripSaveRequest request = new TripSaveRequest(
                "이름",
                LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
                false
        );
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request);
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request);

        // when
        ExtractableResponse<Response> response = restAssuredGet(API_TRIPS_ENDPOINT);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<TripSummaryResponse> data = jsonPath.getList("data", TripSummaryResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("여정 포함 여행 조회")
    void getContainTrip() {
        // given
        Trip trip = Trip.builder()
                .name("여행")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        IntStream.range(0, 3)
                .forEach(i -> createItinerary(trip));

        tripRepository.save(trip);

        restAssuredPostWithToken("/api/comments?tripId=" + trip.getId(),
                createCommentSaveRequest());
        restAssuredPostWithToken("/api/likes?tripId=" + trip.getId());
        restAssuredPostWithToken("/api/likes?tripId=" + trip.getId());

        String url = "/api/trips/" + trip.getId();

        // when
        ExtractableResponse<Response> response = restAssuredGet(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
                () -> assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name()),
                () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
                () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(3),
                () -> assertThat(jsonPath.getList("data.comments").size()).isEqualTo(1),
                () -> assertThat(jsonPath.getInt("data.likeCount")).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("좋아요로 여행 목록 검색")
    void searchByLike() {
        // given
        restAssuredPostBody("/api/signup", createMemberSaveRequest());

        TripSaveRequest request = new TripSaveRequest(
                "name",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                false
        );
        IntStream.range(0, 3)
                .forEach(i -> restAssuredPostWithTokenLogin(API_TRIPS_ENDPOINT, request));

        IntStream.range(1, 3)
                .forEach(i -> restAssuredPostWithTokenLogin("/api/likes?tripId=" + i));

        String url = API_TRIPS_ENDPOINT + "/my-likes";

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("data", TripSummaryResponse.class).size()).isEqualTo(2);
        assertThat(jsonPath.getList("data.id", Long.class)).contains(1L, 2L);
    }

    @Test
    @DisplayName("없는 여행 삭제")
    void deleteTrip_failureException() {
        //given
        String url = "/api/trips/-1";

        //when
        ExtractableResponse<Response> response = restAssuredDeleteWithToken(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(jsonPath.getString("status")).isEqualTo(FAIL.name());
    }

    @Test
    @DisplayName("여행 삭제")
    void deleteTrip() {
        //given
        TripSaveDto request = createTripSaveDto();
        long tripId = restAssuredPostWithToken(API_TRIPS_ENDPOINT, request).jsonPath()
                .getLong("data.id");

        String url = "/api/trips/" + tripId;

        //when
        ExtractableResponse<Response> response = restAssuredDeleteWithTokenLogin(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name());
    }
}
