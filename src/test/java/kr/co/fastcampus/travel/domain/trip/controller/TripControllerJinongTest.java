package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.common.RestAssuredUtils;
import kr.co.fastcampus.travel.common.TokenUtils;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripUpdateRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TripControllerJinongTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여행 수정")
    void editTrip() {
        // given
        TripSaveRequest saveRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        String accessToken = TokenUtils.getAccessToken();
        RestAssuredUtils.restAssuredPostWithToken("/api/trips", saveRequest, accessToken);

        String url = "/api/trips/1";
        TripUpdateRequest updateRequest = new TripUpdateRequest(
                "이름2",
                LocalDate.parse("2011-01-01"),
                LocalDate.parse("2011-01-02"),
                true
        );

        // when
        ExtractableResponse<Response> response
            = RestAssuredUtils.restAssuredPutWithToken(url, updateRequest, accessToken);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름2");
            softly.assertThat(data.startDate()).isEqualTo("2011-01-01");
            softly.assertThat(data.endDate()).isEqualTo("2011-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(true);
            softly.assertThat(
                tripRepository.findById(1L).orElseThrow().getMember().getEmail()
            ).isEqualTo(EMAIL);
        });
    }
}
