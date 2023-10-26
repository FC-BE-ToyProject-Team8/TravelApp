package kr.co.fastcampus.travel;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Trip;

public class TestUtil {
    public static Trip createMockTrip() {
        return Trip.builder()
            .name("tripName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .isForeign(true)
            .build();
    }

    public static ExtractableResponse<Response> findAllTrip() {
        return RestAssured
            .given().log().all()
            .when().get("/api/trips")
            .then().log().all()
            .extract();
    }

    public static ItineraryRequest createMockItineraryRequest() {
        return ItineraryRequest.builder().build();
    }
}
