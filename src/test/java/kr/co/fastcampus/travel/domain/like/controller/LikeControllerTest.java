package kr.co.fastcampus.travel.domain.like.controller;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.co.fastcampus.travel.common.ApiTest;
import kr.co.fastcampus.travel.common.RestAssuredUtils;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class LikeControllerTest extends ApiTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("좋아요 등록")
    void saveLike() {
        // given
        Long tripId = 1L;
        Trip trip = createTrip();
        tripRepository.save(trip);
        Member member = createMember();
        memberRepository.save(member);
        String url = "/api/likes?tripId=1";

        //when
        ExtractableResponse<Response> response
            = RestAssuredUtils.restAssuredPostWithToken(url);


        //then
        Trip foundTrip = tripRepository.findById(tripId).orElseThrow();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(foundTrip.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 취소")
    void deleteLike() {

        // given
        Member member = createMember();
        memberRepository.save(member);

        Long tripId = 1L;
        Trip trip = createTrip();
        tripRepository.save(trip);

        String url = "/api/likes?tripId=" + trip.getId();

        //when
        RestAssuredUtils.restAssuredPostWithToken(url);
        ExtractableResponse<Response> response
            = RestAssuredUtils.restAssuredDeleteWithToken(url);


        //then
        Trip foundTrip = tripRepository.findById(tripId).orElseThrow();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(foundTrip.getLikeCount()).isEqualTo(0);
    }
}
