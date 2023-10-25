package kr.co.fastcampus.travel;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.http.MediaType;

public class TravelUtils {

    public static Trip createTrip() {
        return Trip.builder()
            .name("name")
            .startDate(LocalDate.of(2023, 1, 1))
            .endDate(LocalDate.of(2023, 1, 7))
            .isForeign(true)
            .build();
    }

    public static Itinerary createItinerary(Trip trip) {
        Itinerary itinerary = Itinerary.builder()
            .transportation("목적지")
            .departurePlaceName("출발지")
            .destinationAddress("출발 장소")
            .destinationPlaceName("목적지")
            .destinationAddress("목적 장소")
            .departureAt(LocalDateTime.of(2023, 1, 1, 1, 1))
            .arriveAt(LocalDateTime.of(2023, 7, 1, 15, 0))
            .lodgePlaceName("숙박지")
            .lodgeAddress("숙박 주소")
            .checkInAt(LocalDateTime.of(2023, 1, 1, 1, 1))
            .checkOutAt(LocalDateTime.of(2023, 7, 1, 15, 0))
            .stayPlaceName("체류지")
            .stayAddress("체류 주소")
            .startAt(LocalDateTime.of(2023, 1, 1, 1, 1))
            .endAt(LocalDateTime.of(2023, 7, 1, 15, 0))
            .build();
        itinerary.registerTrip(trip);
        return itinerary;
    }

    public static ExtractableResponse<Response> findAllTripRequest() {
        return RestAssured
            .given().log().all()
            .when().get("/api/trips")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> deleteItineraryRequest(Long id) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParams("itineraryId", id)
            .when().delete("/api/itineraries/{itineraryId}")
            .then().log().all()
            .extract();
    }
}
