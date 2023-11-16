package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createLoginRequest;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.secure.controller.dto.request.LoginReqeust;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
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
        ExtractableResponse<Response> response = restAssuredGetWithToken(API_TRIPS_ENDPOINT);

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
    @DisplayName("좋아요로 여행 목록 검색")
    void searchByLike() {
        // given
        restAssuredPostBody("/signup", createMemberSaveRequest());

        LoginReqeust login = createLoginRequest();
        TripSaveRequest request = new TripSaveRequest(
                "name",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                false
        );
        IntStream.range(0, 3)
                .forEach(i -> restAssuredPostWithToken(API_TRIPS_ENDPOINT, request, login));

        IntStream.range(1, 3)
                .forEach(i -> restAssuredPostWithToken("/api/likes?tripId=" + i, login));

        String url = API_TRIPS_ENDPOINT + "/search-by-like";

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("data", TripSummaryResponse.class).size()).isEqualTo(2);
        assertThat(jsonPath.getList("data.id", Long.class)).contains(1L, 2L);
    }
}
