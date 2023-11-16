package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGet;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithTokenLogin;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
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
import kr.co.fastcampus.travel.common.response.Status;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
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
                .forEach(i -> {
                    Itinerary itinerary = Itinerary.builder().build();
                    itinerary.registerTrip(trip);
                });

        tripRepository.save(trip);

        String url = "/api/trips/" + trip.getId();

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
                () -> assertThat(jsonPath.getString("status")).isEqualTo(Status.SUCCESS.name()),
                () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
                () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(3)
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
}
