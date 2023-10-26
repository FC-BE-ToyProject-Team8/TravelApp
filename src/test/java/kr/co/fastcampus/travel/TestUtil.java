package kr.co.fastcampus.travel;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.controller.request.RouteRequest;
import kr.co.fastcampus.travel.controller.request.StayRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.http.MediaType;

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

    public static ExtractableResponse<Response> findAndEditItinerary(Long id,
        ItineraryRequest request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParams("id", id)
            .body(request)
            .when()
            .put("/api/itineraries/{id}")
            .then().log().all()
            .extract();
    }

    public static Itinerary createMockItinerary(Trip trip, Route route, Lodge lodge, Stay stay) {
        Itinerary itinerary = Itinerary.builder()
            .route(route == null ? null : createMockRoute())
            .lodge(lodge == null ? null : createMockLodge())
            .stay(stay == null ? null : createMockStay())
            .build();
        itinerary.registerTrip(trip);
        return itinerary;
    }

    public static Route createMockRoute() {
        return Route.builder()
            .transportation("지하철")
            .departurePlaceName("우리집")
            .departureAddress("서울")
            .destinationPlaceName("해운대")
            .destinationAddress("부산")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }

    public static Lodge createMockLodge() {
        return Lodge.builder()
            .placeName("호텔")
            .address("부산 @@@")
            .checkOutAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkInAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();
    }

    public static Stay createMockStay() {
        return Stay.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }

    public static ItineraryRequest createMockItineraryRequest() {
        return ItineraryRequest.builder().build();
    }

    public static ItineraryRequest createMockItineraryRequest(RouteRequest route,
        LodgeRequest lodge, StayRequest stay) {
        return ItineraryRequest.builder()
            .route(route == null ? null : createMockRouteRequest())
            .lodge(lodge == null ? null : createMockLodgeRequest())
            .stay(stay == null ? null : createMockStayRequest())
            .build();
    }

    public static RouteRequest createMockRouteRequest() {
        return RouteRequest.builder()
            .transportation("이동수단 업데이트")
            .departurePlaceName("출발지 업데이트")
            .departureAddress("출발 주소 업데이트")
            .destinationPlaceName("목적지 업데이트")
            .destinationAddress("목적지 주소 업데이트")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }

    public static LodgeRequest createMockLodgeRequest() {
        return LodgeRequest.builder()
            .placeName("장소 업데이트")
            .address("주소 업데이트")
            .checkInAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkOutAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();
    }

    public static StayRequest createMockStayRequest() {
        return StayRequest.builder()
            .placeName("장소 업데이트")
            .address("주소 업데이트")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }
}
