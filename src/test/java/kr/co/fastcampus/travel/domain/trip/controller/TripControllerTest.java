package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMemberSaveRequest;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredDeleteWithTokenLogin;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGet;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostBody;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithToken;
import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredPostWithTokenLogin;
import static kr.co.fastcampus.travel.common.TravelTestUtils.API_TRIPS_ENDPOINT;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentSaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerarySaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripSaveDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripWithMember;
import static kr.co.fastcampus.travel.common.response.Status.FAIL;
import static kr.co.fastcampus.travel.common.response.Status.SUCCESS;
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
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.common.RestAssuredUtils;
import kr.co.fastcampus.travel.common.TokenUtils;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripUpdateRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class TripControllerTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("여행 등록")
    void addTrip() {
        // given
        String url = "/api/trips";
        TripSaveRequest request = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        // when
        ExtractableResponse<Response> response
            = restAssuredPostWithToken(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름");
            softly.assertThat(data.startDate().toString()).isEqualTo("2010-01-01");
            softly.assertThat(data.endDate().toString()).isEqualTo("2010-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(false);
            softly.assertThat(
                tripRepository.findById(data.id()).orElseThrow().getMember().getEmail()
            ).isEqualTo(EMAIL);
        });
    }

    @Test
    @DisplayName("여행 수정")
    void editTrip() {
        // given
        TripSaveRequest saveRequest = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        String accessToken = TokenUtils.getAccessToken();
        restAssuredPostWithToken("/api/trips", saveRequest, accessToken);

        String url = "/api/trips/1";
        TripUpdateRequest updateRequest = new TripUpdateRequest(
            "이름2",
            LocalDate.parse("2011-01-01"),
            LocalDate.parse("2011-01-02"),
            true
        );

        // when
        ExtractableResponse<Response> response
            = RestAssuredUtils.restAssuredPutWithToken(url, updateRequest, accessToken);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름2");
            softly.assertThat(data.startDate()).isEqualTo("2011-01-01");
            softly.assertThat(data.endDate()).isEqualTo("2011-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(true);
            softly.assertThat(
                tripRepository.findById(1L).orElseThrow().getMember().getEmail()
            ).isEqualTo(EMAIL);
        });
    }

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        TripSaveRequest request = new TripSaveRequest(
            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request);
        restAssuredPostWithToken(API_TRIPS_ENDPOINT, request);

        // when
        ExtractableResponse<Response> response = restAssuredGet(API_TRIPS_ENDPOINT);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<TripSummaryResponse> data = jsonPath.getList("data", TripSummaryResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("여정 포함 여행 조회")
    void getContainTrip() {
        // given
        Trip trip = Trip.builder()
            .name("여행")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .build();

        IntStream.range(0, 3)
            .forEach(i -> createItinerary(trip));

        tripRepository.save(trip);

        restAssuredPostWithToken("/api/comments?tripId=" + trip.getId(),
            createCommentSaveRequest());
        restAssuredPostWithToken("/api/likes?tripId=" + trip.getId());
        restAssuredPostWithToken("/api/likes?tripId=" + trip.getId());

        String url = "/api/trips/" + trip.getId();

        // when
        ExtractableResponse<Response> response = restAssuredGet(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertAll(
            () -> assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name()),
            () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
            () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(3),
            () -> assertThat(jsonPath.getList("data.comments").size()).isEqualTo(1),
            () -> assertThat(jsonPath.getInt("data.likeCount")).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("좋아요로 여행 목록 검색")
    void searchByLike() {
        // given
        restAssuredPostBody("/api/signup", createMemberSaveRequest());

        TripSaveRequest request = new TripSaveRequest(
            "name",
            LocalDate.now(),
            LocalDate.now().plusDays(3),
            false
        );
        IntStream.range(0, 3)
            .forEach(i -> restAssuredPostWithTokenLogin(API_TRIPS_ENDPOINT, request));

        IntStream.range(1, 3)
            .forEach(i -> restAssuredPostWithTokenLogin("/api/likes?tripId=" + i));

        String url = API_TRIPS_ENDPOINT + "/my-likes";

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        JsonPath jsonPath = response.jsonPath();
        assertThat(jsonPath.getList("data", TripSummaryResponse.class).size()).isEqualTo(2);
        assertThat(jsonPath.getList("data.id", Long.class)).contains(1L, 2L);
    }

    @Test
    @DisplayName("없는 여행 삭제")
    void deleteTrip_failureException() {
        //given
        String url = "/api/trips/-1";

        //when
        ExtractableResponse<Response> response = restAssuredDeleteWithToken(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(jsonPath.getString("status")).isEqualTo(FAIL.name());
    }

    @Test
    @DisplayName("여행 삭제")
    void deleteTrip() {
        //given
        TripSaveDto request = createTripSaveDto();
        long tripId = restAssuredPostWithToken(API_TRIPS_ENDPOINT, request).jsonPath()
            .getLong("data.id");

        String url = "/api/trips/" + tripId;

        //when
        ExtractableResponse<Response> response = restAssuredDeleteWithTokenLogin(url);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name());
    }

    @Test
    @DisplayName("여정 복수 등록")
    void addItineraries() {
        // given
        String saveUrl = "/api/trips";
        TripSaveRequest tripSaveRequest = new TripSaveRequest(

            "이름",
            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
            false
        );

        RestAssuredUtils.restAssuredPostWithToken(saveUrl, tripSaveRequest);

        String url = "/api/itineraries?tripId=1";
        List<ItinerarySaveRequest> itineraries = IntStream.range(0, 3)
            .mapToObj(i -> createItinerarySaveRequest())
            .toList();
        ItinerariesSaveRequest request = new ItinerariesSaveRequest(1L, itineraries);

        //when
        ExtractableResponse<Response> response
            = RestAssuredUtils.restAssuredPostWithToken(url, request);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<ItineraryResponse> data = jsonPath.getList("data", ItineraryResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(3);
        });
    }

    @Test
    @DisplayName("사용자 닉네임으로 여행 검색")
    void searchByNickname() {
        // given
        Member member = createMember();
        memberRepository.save(member);
        List<Trip> saveTrips = IntStream.range(0, 10)
            .mapToObj(i -> saveTripWithMember(member)).toList();
        String url =
            "/api/trips/search-by-nickname?query=" + member.getNickname() + "&page=" + 1;

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<TripSummaryResponse> content =
            jsonPath.getList("data.content", TripSummaryResponse.class);
        int totalPages = jsonPath.getInt("data.totalPages");
        int totalElements = jsonPath.getInt("data.totalElements");
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(content.size()).isEqualTo(5);
            softly.assertThat(totalPages).isEqualTo(2);
            softly.assertThat(totalElements).isEqualTo(10);
        });
    }

    private Trip saveTripWithMember(Member member) {
        Trip trip = createTripWithMember(member);
        return tripRepository.save(trip);
    }
}
