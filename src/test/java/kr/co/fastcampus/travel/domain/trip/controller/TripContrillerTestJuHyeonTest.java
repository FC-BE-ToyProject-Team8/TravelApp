package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerarySaveRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripWithMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.common.RestAssuredUtils;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class TripContrillerTestJuHyeonTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

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
