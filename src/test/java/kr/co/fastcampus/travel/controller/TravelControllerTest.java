package kr.co.fastcampus.travel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.response.Status;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.controller.request.RouteRequest;
import kr.co.fastcampus.travel.controller.request.StayRequest;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TravelControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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
    @DisplayName("여행과 여정 조회")
    void getTrip() {
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
    @DisplayName("여행과 여정 조회")
    void editItinerary() {
        //given
        String url = "/api/itineraries/1";

        Lodge lodge = Lodge.builder()
            .placeName("호텔")
            .address("부산 @@@")
            .checkOutAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkInAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();

        Stay stay = Stay.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        Route route = Route.builder()
            .transportation("지하철")
            .departurePlaceName("우리집")
            .departureAddress("서울")
            .destinationPlaceName("해운대")
            .destinationAddress("부산")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        itineraryRepository.save(
            Itinerary.builder()
                .route(route)
                .lodge(lodge)
                .stay(stay)
                .build()
        );

        LodgeRequest lodge2 = LodgeRequest.builder()
            .placeName("글램핑")
            .address("부산 @@@")
            .checkInAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkOutAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();

        StayRequest stay2 = StayRequest.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 50))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        RouteRequest route2 = RouteRequest.builder()
            .transportation("택시")
            .departurePlaceName("우리집")
            .departureAddress("서울")
            .destinationPlaceName("해운대")
            .destinationAddress("부산")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        ItineraryRequest request = ItineraryRequest.builder()
            .lodge(lodge2)
            .route(route2)
            .stay(stay2)
            .build();

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .put(url)
            .then().log().all()
            .extract();

        //then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        ItineraryResponse data = jsonPath.getObject("data", ItineraryResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.lodge().placeName()).isEqualTo("글램핑"); // 수정된 부분
            softly.assertThat(data.lodge().checkOutAt()).isEqualTo("2023-01-02T11:00"); // 수정x
            softly.assertThat(data.route().transportation()).isEqualTo("택시"); // 수정된 부분
            softly.assertThat(data.route().departurePlaceName()).isEqualTo("우리집"); //수정x
            softly.assertThat(data.stay().startAt()).isEqualTo("2023-01-01T11:30:50"); // 수정된 부분
            softly.assertThat(data.stay().placeName()).isEqualTo("한국"); //수정x
        });
    }
}
