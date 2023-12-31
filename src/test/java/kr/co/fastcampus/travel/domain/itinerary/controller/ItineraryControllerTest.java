package kr.co.fastcampus.travel.domain.itinerary.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPutWithToken;
import static kr.co.fastcampus.travel.common.TokenUtils.getAccessToken;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_ITINERARIES_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerarySaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItineraryUpdateRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createLodgeSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createLodgeUpdateRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRouteSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRouteUpdateRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createStaySaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createStayUpdateRequest;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.ItineraryUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.LodgeUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.RouteUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.StayUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class ItineraryControllerTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여정 수정")
    void editItinerary() {
        //given
        String url = "/api/itineraries/1";

        TripSaveRequest saveTripRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        List<ItinerarySaveRequest> itineraries = IntStream.range(0, 3)
            .mapToObj(i -> createItinerarySaveRequest())
            .toList();
        ItinerariesSaveRequest saveItinerariesRequest = new ItinerariesSaveRequest(1L, itineraries);

        restAssuredPostWithToken(API_TRIPS_ENDPOINT, saveTripRequest);
        restAssuredPostWithToken(API_ITINERARIES_ENDPOINT, saveItinerariesRequest);

        LodgeUpdateRequest lodge = createLodgeUpdateRequest();
        StayUpdateRequest stay = createStayUpdateRequest();
        RouteUpdateRequest route = createRouteUpdateRequest();
        ItineraryUpdateRequest request = createItineraryUpdateRequest(route, lodge, stay);

        //when
        ExtractableResponse<Response> response =
            restAssuredPutWithToken(url, request);

        //then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        ItineraryResponse data = jsonPath.getObject("data", ItineraryResponse.class);

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.route().transportation()).isEqualTo(Transportation.SUBWAY);
            softly.assertThat(data.route().departurePlaceName()).isEqualTo("출발지 업데이트");
            softly.assertThat(data.lodge().placeName()).isEqualTo("장소 업데이트");
            softly.assertThat(data.lodge().checkOutAt()).isEqualTo("2023-01-02T11:00");
            softly.assertThat(data.stay().startAt()).isEqualTo("2023-01-01T11:30:30");
            softly.assertThat(data.stay().placeName()).isEqualTo("장소 업데이트");
        });
    }

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
        Trip findTrip = tripRepository.findFetchDetailById(1L).get();
        List<Itinerary> itineraries = findTrip.getItineraries();
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(itineraries.size()).isEqualTo(1);
            softly.assertThat(findTrip.getMember().getEmail()).isEqualTo(EMAIL);
        });
    }
}
