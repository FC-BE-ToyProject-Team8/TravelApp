package kr.co.fastcampus.travel.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.RouteRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.StayRequest;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.springframework.http.MediaType;

public class TravelTestUtils {

    private TravelTestUtils() {
    }

    public static Trip createTrip() {
        return Trip.builder()
            .name("tripName")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .isForeign(true)
            .build();
    }

    public static ItineraryRequest createItineraryRequest() {
        return ItineraryRequest.builder().build();
    }

    public static ItineraryRequest createItineraryRequest(
        RouteRequest route,
        LodgeRequest lodge,
        StayRequest stay
    ) {
        return ItineraryRequest.builder()
            .route(route)
            .lodge(lodge)
            .stay(stay)
            .build();
    }

    public static ExtractableResponse<Response> putAndExtractResponse(Long itineraryId,
                                                                      ItineraryRequest request,
                                                                      String url) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParams("itineraryId", itineraryId)
            .body(request)
            .when()
            .put(url)
            .then().log().all()
            .extract();
    }

    public static Itinerary createItinerary(Trip trip) {
        Route route = createRoute();
        Lodge lodge = createLodge();
        Stay stay = createStay();

        Itinerary itinerary = Itinerary.builder()
            .route(route)
            .lodge(lodge)
            .stay(stay)
            .build();
        itinerary.registerTrip(trip);
        return itinerary;
    }

    public static Itinerary createItinerary(Trip trip, Route route, Lodge lodge, Stay stay) {
        Itinerary itinerary = Itinerary.builder()
            .route(route)
            .lodge(lodge)
            .stay(stay)
            .build();
        itinerary.registerTrip(trip);
        return itinerary;
    }

    public static Route createRoute() {
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

    public static Lodge createLodge() {
        return Lodge.builder()
            .placeName("호텔")
            .address("부산 @@@")
            .checkOutAt(LocalDateTime.of(2023, 1, 1, 15, 0))
            .checkInAt(LocalDateTime.of(2023, 1, 2, 11, 0))
            .build();
    }

    public static Stay createStay() {
        return Stay.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }

    public static RouteRequest createRouteRequest() {
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

    public static LodgeRequest createLodgeRequest() {
        return LodgeRequest.builder()
            .placeName("장소 업데이트")
            .address("주소 업데이트")
            .checkInAt(LocalDateTime.of(2023, 1, 1, 15, 0))
            .checkOutAt(LocalDateTime.of(2023, 1, 2, 11, 0))
            .build();
    }

    public static StayRequest createStayRequest() {
        return StayRequest.builder()
            .placeName("장소 업데이트")
            .address("주소 업데이트")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();
    }

    public static ExtractableResponse<Response> requestFindAllTripApi() {
        return RestAssured
            .given().log().all()
            .when().get("/api/trips")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestDeleteApi(String url) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(url)
            .then().log().all()
            .extract();
    }
}
