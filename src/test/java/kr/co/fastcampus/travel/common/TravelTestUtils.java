package kr.co.fastcampus.travel.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentSaveRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentUpdateRequest;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentSaveDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.LodgeSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.RouteSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.StaySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.ItineraryUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.LodgeUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.RouteUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.StayUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.LodgeSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.RouteSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.StaySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.ItineraryUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.LodgeUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.RouteUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.StayUpdateDto;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import org.springframework.http.MediaType;

public class TravelTestUtils {

    public static final String API_TRIPS_ENDPOINT = "/api/trips";
    public static final String API_ITINERARIES_ENDPOINT = "/api/itineraries";

    private TravelTestUtils() {
    }

    public static Comment createComment(Member member) {
        return Comment.builder()
                .member(member)
                .content("test comment")
                .build();
    }

    public static CommentSaveRequest createCommentSaveRequest() {
        return CommentSaveRequest.builder()
                .content("test comment")
                .build();
    }

    public static CommentUpdateRequest createCommentUpdateRequest() {
        return CommentUpdateRequest.builder()
                .content("update comment")
                .build();
    }

    public static CommentSaveDto createCommentSaveDto() {
        return CommentSaveDto.builder()
                .content("test comment")
                .build();
    }

    public static CommentUpdateDto createCommentUpdateDto() {
        return CommentUpdateDto.builder()
                .content("update comment")
                .build();
    }

    public static Member createMember() {
        return Member.builder()
                .email("test@email.com")
                .name("tester")
                .nickname("testNick")
                .password("123")
                .build();
    }

    public static Trip createTripWithMember(Member member) {
        return Trip.builder()
                .name("tripName")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .isForeign(true)
                .member(member)
                .build();
    }

    public static Trip createTrip() {
        return Trip.builder()
                .name("tripName")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .isForeign(true)
                .build();
    }

    public static Like createLike(Trip trip, Member member) {
        return Like.builder()
                .trip(trip)
                .member(member)
                .build();
    }

    public static TripSaveDto createTripSaveDto() {
        return TripSaveDto.builder()
                .name("tripName")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .isForeign(true)
                .build();
    }

    public static ItinerarySaveDto createItinerarySaveDto() {
        return createItinerarySaveDto(null, null, null);
    }

    public static ItinerarySaveDto createItinerarySaveDto(
            RouteSaveDto route,
            LodgeSaveDto lodge,
            StaySaveDto stay
    ) {
        return new ItinerarySaveDto(route, lodge, stay);
    }

    public static ItineraryUpdateDto createItineraryUpdateDto() {
        return createItineraryUpdateDto(null, null, null);
    }

    public static ItineraryUpdateDto createItineraryUpdateDto(
            RouteUpdateDto route,
            LodgeUpdateDto lodge,
            StayUpdateDto stay
    ) {
        return new ItineraryUpdateDto(route, lodge, stay);
    }

    public static ItinerarySaveRequest createItinerarySaveRequest() {
        return createItinerarySaveRequest(null, null, null);
    }

    public static ItinerarySaveRequest createItinerarySaveRequest(
            RouteSaveRequest route,
            LodgeSaveRequest lodge,
            StaySaveRequest stay
    ) {
        return new ItinerarySaveRequest(route, lodge, stay);
    }

    public static ItineraryUpdateRequest createItineraryUpdateRequest() {
        return createItineraryUpdateRequest(null, null, null);
    }

    public static ItineraryUpdateRequest createItineraryUpdateRequest(
            RouteUpdateRequest route,
            LodgeUpdateRequest lodge,
            StayUpdateRequest stay
    ) {
        return new ItineraryUpdateRequest(route, lodge, stay);
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
                .transportation(Transportation.BUS)
                .departurePlaceName("우리집")
                .departureAddress("서울")
                .destinationPlaceName("해운대")
                .destinationAddress("부산")
                .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
                .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 40))
                .build();
    }

    public static Lodge createLodge() {
        return Lodge.builder()
                .placeName("호텔")
                .address("부산 @@@")
                .checkInAt(LocalDateTime.of(2023, 1, 1, 11, 0))
                .checkOutAt(LocalDateTime.of(2023, 1, 2, 15, 0))
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

    public static RouteSaveDto createRouteSaveDto() {
        return new RouteSaveDto(
                Transportation.BUS,
                "우리집",
                "서울",
                "해운대",
                "부산",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static LodgeSaveDto createLodgeSaveDto() {
        return new LodgeSaveDto(
                "호텔",
                "부산 @@@",
                LocalDateTime.of(2023, 1, 1, 15, 0),
                LocalDateTime.of(2023, 1, 2, 11, 0)
        );
    }

    public static StaySaveDto createStaySaveDto() {
        return new StaySaveDto(
                "한국",
                "대한민국",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static RouteUpdateDto createRouteUpdateDto() {
        return new RouteUpdateDto(
                Transportation.SUBWAY,
                "출발지 업데이트",
                "출발 주소 업데이트",
                "목적지 업데이트",
                "목적지 주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static LodgeUpdateDto createLodgeUpdateDto() {
        return new LodgeUpdateDto(
                "장소 업데이트",
                "주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 15, 0),
                LocalDateTime.of(2023, 1, 2, 11, 0)
        );
    }

    public static StayUpdateDto createStayUpdateDto() {
        return new StayUpdateDto(
                "장소 업데이트",
                "주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static RouteSaveRequest createRouteSaveRequest() {
        return new RouteSaveRequest(
                Transportation.BUS,
                "우리집",
                "서울",
                "해운대",
                "부산",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static LodgeSaveRequest createLodgeSaveRequest() {
        return new LodgeSaveRequest(
                "호텔",
                "부산 @@@",
                LocalDateTime.of(2023, 1, 1, 15, 0),
                LocalDateTime.of(2023, 1, 2, 11, 0)
        );
    }

    public static StaySaveRequest createStaySaveRequest() {
        return new StaySaveRequest(
                "한국",
                "대한민국",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static RouteUpdateRequest createRouteUpdateRequest() {
        return new RouteUpdateRequest(
                Transportation.SUBWAY,
                "출발지 업데이트",
                "출발 주소 업데이트",
                "목적지 업데이트",
                "목적지 주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static LodgeUpdateRequest createLodgeUpdateRequest() {
        return new LodgeUpdateRequest(
                "장소 업데이트",
                "주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 15, 0),
                LocalDateTime.of(2023, 1, 2, 11, 0)
        );
    }

    public static StayUpdateRequest createStayUpdateRequest() {
        return new StayUpdateRequest(
                "장소 업데이트",
                "주소 업데이트",
                LocalDateTime.of(2023, 1, 1, 11, 30, 30),
                LocalDateTime.of(2023, 1, 1, 11, 30, 30)
        );
    }

    public static ExtractableResponse<Response> requestFindAllTripApi() {
        return RestAssured
                .given().log().all()
                .when().get(API_TRIPS_ENDPOINT)
                .then().log().all()
                .extract();

    }

    public static ExtractableResponse<Response> requestDeleteApi(String url) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
