package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.TravelTestUtils.createItineraryRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createLodgeRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createMockTrip;
import static kr.co.fastcampus.travel.TravelTestUtils.createRouteRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createStayRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.findAllTrip;
import static kr.co.fastcampus.travel.TravelTestUtils.putAndExtractResponse;
import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripSummaryResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.ApiTest;
import kr.co.fastcampus.travel.common.response.Status;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.controller.request.RouteRequest;
import kr.co.fastcampus.travel.controller.request.StayRequest;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.controller.response.TripSummaryResponse;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TravelControllerTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Test
    @DisplayName("여행 등록")
    void addTrip() {
        // given
        String url = "/api/trips";
        TripRequest request = new TripRequest(
            "이름", "2010-01-01", "2010-01-02", false
        );

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .post(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름");
            softly.assertThat(data.startAt().toString()).isEqualTo("2010-01-01");
            softly.assertThat(data.endAt().toString()).isEqualTo("2010-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(false);
        });
    }

    @Test
    @DisplayName("여정 없는 여행 조회")
    void getOnlyTrip() {
        // given
        String url = "/api/trips/{id}";
        Trip trip = Trip.builder()
            .name("여행")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .build();

        tripRepository.save(trip);

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .pathParams("id", trip.getId())
            .when().get(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
            () -> assertThat(jsonPath.getString("status")).isEqualTo(Status.SUCCESS.name()),
            () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
            () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("여정 수정")
    void editItinerary() {
        //given
        String url = "/api/itineraries/{id}";
        Trip trip = createMockTrip();
        tripRepository.save(trip);

        Itinerary itinerary = createItinerary(trip);
        itineraryRepository.save(itinerary);

        LodgeRequest lodge2 = createLodgeRequest();
        StayRequest stay2 = createStayRequest();
        RouteRequest route2 = createRouteRequest();
        ItineraryRequest request = createItineraryRequest(route2, lodge2, stay2);

        //when
        ExtractableResponse<Response> response =
            putAndExtractResponse(itinerary.getId(), request, url);

        //then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        ItineraryResponse data = jsonPath.getObject("data", ItineraryResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.route().transportation()).isEqualTo("이동수단 업데이트");
            softly.assertThat(data.route().departurePlaceName()).isEqualTo("출발지 업데이트");
            softly.assertThat(data.lodge().placeName()).isEqualTo("장소 업데이트");
            softly.assertThat(data.lodge().checkOutAt()).isEqualTo("2023-01-02T11:00");
            softly.assertThat(data.stay().startAt()).isEqualTo("2023-01-01T11:30:30");
            softly.assertThat(data.stay().placeName()).isEqualTo("장소 업데이트");
        });
    }

    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        List<Trip> saveTrips = IntStream.range(0, 2).mapToObj(i -> saveTrip()).toList();

        // when
        ExtractableResponse<Response> response = findAllTrip();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<TripSummaryResponse> data = jsonPath.getList("data", TripSummaryResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(2);
            softly.assertThat(data).contains(toTripSummaryResponse(saveTrips.get(0)));
            softly.assertThat(data).contains(toTripSummaryResponse(saveTrips.get(1)));
        });
    }

    @Test
    @DisplayName("여정 포함 여행 조회")
    void getContainTrip() {
        // given
        String url = "/api/trips/{id}";
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

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .pathParams("id", trip.getId())
            .when().get(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
            () -> assertThat(jsonPath.getString("status")).isEqualTo(Status.SUCCESS.name()),
            () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
            () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(3)
        );
    }

    private Trip saveTrip() {
        Trip trip = createMockTrip();
        tripRepository.save(trip);
        return trip;
    }
}
