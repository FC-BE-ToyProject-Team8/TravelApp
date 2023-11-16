package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripPageResponseDto;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

class TripControllerTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여행 등록")
    void addTrip() {
        // given
        String url = "/api/trips";
        TripSaveRequest request = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        // when
        ExtractableResponse<Response> response
            = restAssuredPostWithToken(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름");
            softly.assertThat(data.startDate().toString()).isEqualTo("2010-01-01");
            softly.assertThat(data.endDate().toString()).isEqualTo("2010-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(false);
            softly.assertThat(
                tripRepository.findById(data.id()).orElseThrow().getMember().getEmail()
            ).isEqualTo(EMAIL);
        });
    }

    @Test
    @DisplayName("여행 이름으로 여행 검색")
    void searchByTripName() {
        //given
        TripSaveRequest request1 = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        TripSaveRequest request2 = new TripSaveRequest(
            "테스트",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request1);
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request1);
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request2);

        String query = "이름";
        Pageable pageable = PageRequest.of(0, 3);
        String url = API_TRIPS_ENDPOINT
            + String.format("/search-by-trip-name?query=%s&page=%d&size=%d",
            query, pageable.getPageNumber(), pageable.getPageSize());

        //when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripPageResponseDto data = jsonPath.getObject("data", TripPageResponseDto.class);
        List<TripSummaryResponse> content = data.content();

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.pageNum()).isEqualTo(1);
            softly.assertThat(data.pageSize()).isEqualTo(3);
            softly.assertThat(data.totalElements()).isEqualTo(2);
        });
    }
}
