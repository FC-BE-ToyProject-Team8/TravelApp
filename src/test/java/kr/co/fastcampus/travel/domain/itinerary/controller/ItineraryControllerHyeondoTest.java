package kr.co.fastcampus.travel.domain.itinerary.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.TokenUtils.getAccessToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_ITINERARIES_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerarySaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createLodgeSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRouteSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createStaySaveRequest;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class ItineraryControllerHyeondoTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여정 삭제")
    void deleteItinerary() {
        //given
        String accessToken = getAccessToken();
        TripSaveRequest tripRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, tripRequest, accessToken);
        ItinerarySaveRequest itinerary =
            createItinerarySaveRequest(
                createRouteSaveRequest(),
                createLodgeSaveRequest(),
                createStaySaveRequest()
            );
        ItinerariesSaveRequest request = new ItinerariesSaveRequest(1L, List.of(itinerary));

        restAssuredPostWithToken(API_ITINERARIES_ENDPOINT, request, accessToken);
        restAssuredPostWithToken(API_ITINERARIES_ENDPOINT, request, accessToken);

        //when
        ExtractableResponse<Response> response =
            restAssuredDeleteWithToken(API_ITINERARIES_ENDPOINT + "/1", accessToken);

        //then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        Trip findTrip = tripRepository.findFetchItineraryById(1L).get();
        List<Itinerary> itineraries = findTrip.getItineraries();
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(itineraries.size()).isEqualTo(1);
            softly.assertThat(findTrip.getMember().getEmail()).isEqualTo(EMAIL);
        });
    }
}
