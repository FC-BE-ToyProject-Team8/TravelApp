package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.TravelTestUtils.createItineraryRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createLodgeRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createRouteRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createStayRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createTrip;
import static kr.co.fastcampus.travel.TravelTestUtils.putAndExtractResponse;
import static kr.co.fastcampus.travel.TravelTestUtils.requestDeleteApi;
import static kr.co.fastcampus.travel.TravelTestUtils.requestFindAllTripApi;
import static kr.co.fastcampus.travel.common.response.Status.FAIL;
import static kr.co.fastcampus.travel.common.response.Status.SUCCESS;
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
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
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
        Trip trip = createTrip();
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

    @Test
    @DisplayName("여행 수정")
    void editTrip() {
        // given
        tripRepository.save(
            Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
                .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
                .build()
        );

        String url = "/api/trips/1";
        TripRequest request = TripRequest.builder()
            .name("이름2")
            .startDate(LocalDate.parse("2011-01-01"))
            .endDate(LocalDate.parse("2011-01-02"))
            .isForeign(true)
            .build();

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .put(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름2");
            softly.assertThat(data.startAt()).isEqualTo("2011-01-01");
            softly.assertThat(data.endAt()).isEqualTo("2011-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(true);
        });
    }

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        List<Trip> saveTrips = IntStream.range(0, 2).mapToObj(i -> saveTrip()).toList();

        // when
        ExtractableResponse<Response> response = requestFindAllTripApi();

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

    @Test
    @DisplayName("여정 복수 등록")
    void addItineraries() {
        // given
        Trip trip = createTrip();
        tripRepository.save(trip);
        String url = "/api/trips/1/itineraries";
        List<ItineraryRequest> request = IntStream.range(0, 3)
            .mapToObj(i -> createItineraryRequest())
            .toList();

        //when
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
            softly.assertThat(data.id()).isEqualTo(1);
            softly.assertThat(data.name()).isEqualTo("tripName");
            softly.assertThat(data.itineraries().size()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("없는 여행 삭제")
    void deleteTrip_failureException() {
        //given
        String url = "/api/trips/-1";

        //when
        ExtractableResponse<Response> response = requestDeleteApi(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("status")).isEqualTo(FAIL.name());
    }

    @Test
    @DisplayName("여행 삭제")
    void deleteTrip() {
        //given
        Trip trip = saveTrip();
        String url = "/api/trips/" + trip.getId();

        //when
        ExtractableResponse<Response> response = requestDeleteApi(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name());
    }

    @Test
    @DisplayName("여정 삭제")
    void deleteItinerary() {
        //given
        Trip trip = saveTrip();
        saveItinerary(trip);
        Itinerary itinerary = saveItinerary(trip);

        //when
        String url = "/api/itineraries/" + itinerary.getId();
        requestDeleteApi(url);

        //then
        Trip findTrip = tripRepository.findFetchItineraryById(trip.getId()).get();
        List<Itinerary> itineraries = findTrip.getItineraries();
        assertThat(itineraries.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 여정 삭제")
    void deleteNoneItinerary() {
        //given
        Trip trip = saveTrip();
        saveItinerary(trip);
        saveItinerary(trip);

        //when
        String url = "/api/itineraries/5";
        ExtractableResponse<Response> response = requestDeleteApi(url);

        //then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(response.jsonPath().getString("status")).isEqualTo("FAIL");
            softly.assertThat(response.jsonPath().getString("errorMessage"))
                .isEqualTo("존재하지 않는 엔티티입니다.");
        });

    }

    private Trip saveTrip() {
        Trip trip = createTrip();
        return tripRepository.save(trip);
    }

    private Itinerary saveItinerary(Trip trip) {
        Itinerary itinerary = createItinerary(trip);
        return itineraryRepository.save(itinerary);
    }
}
