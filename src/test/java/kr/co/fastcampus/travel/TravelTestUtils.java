package kr.co.fastcampus.travel;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.http.MediaType;

public class TravelTestUtils {

    private TravelTestUtils() {
    }

    public static Trip createMockTrip() {
        return Trip.builder()
            .name("tripName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .isForeign(true)
            .build();
    }

    public static Itinerary createMockItinerary(Trip trip) {
        Itinerary itinerary = Itinerary
            .builder()
            .route(
                Route.builder()
                    .transportation("목적지")
                    .departurePlaceName("출발지")
                    .destinationAddress("출발 장소")
                    .destinationPlaceName("목적지")
                    .destinationAddress("목적 장소")
                    .departureAt(LocalDateTime.of(2023, 1, 1, 1, 1))
                    .arriveAt(LocalDateTime.of(2023, 7, 1, 15, 0))
                    .build())
            .lodge(
                Lodge.builder()
                    .placeName("숙박지")
                    .address("숙박 주소")
                    .checkInAt(LocalDateTime.of(2023, 1, 1, 1, 1))
                    .checkOutAt(LocalDateTime.of(2023, 7, 1, 15, 0))
                    .build())
            .stay(
                Stay.builder()
                    .placeName("체류지")
                    .address("체류 주소")
                    .startAt(LocalDateTime.of(2023, 1, 1, 1, 1))
                    .endAt(LocalDateTime.of(2023, 7, 1, 15, 0))
                    .build())
            .build();
        itinerary.registerTrip(trip);
        return itinerary;
    }

    public static ExtractableResponse<Response> requestFindAllTripApi() {
        return RestAssured
            .given().log().all()
            .when().get("/api/trips")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestDeleteItineraryApi(Long id, String url) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParams("itineraryId", id)
            .when().delete(url)
            .then().log().all()
            .extract();
    }
}
