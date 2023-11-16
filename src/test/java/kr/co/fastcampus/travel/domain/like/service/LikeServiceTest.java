package kr.co.fastcampus.travel.domain.like.service;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

import java.time.LocalDate;
import kr.co.fastcampus.travel.common.exception.DuplicatedLikeException;
import kr.co.fastcampus.travel.common.exception.InvalidLikeCancelException;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.like.repository.LikeRepository;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private TripService tripService;

    @Mock
    private MemberService memberService;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("좋아요 등록")
    void saveLike() {

        //given
        Long tripId = 1L;
        Trip trip = createTrip();
        Member member = createMember();
        given(tripService.findById(tripId)).willReturn(trip);
        given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);

        //when
        likeService.saveLike(tripId, member.getEmail());

        //then
        verify(likeRepository, atLeastOnce()).save(any(Like.class));
    }

    @Test
    @DisplayName("좋아요 실패")
    void saveLike_fail() {

        //given
        Long tripId = 1L;
        Trip trip = createTrip();
        given(tripService.findById(tripId)).willReturn(trip);
        Member member = createMember();
        given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
        given(likeRepository.existsByTripAndMember(trip, member)).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> likeService.saveLike(tripId, member.getEmail()))
            .isInstanceOf(DuplicatedLikeException.class);
    }

    @Test
    @DisplayName("좋아요 취소")
    void deleteLike() {

        //given
        Long tripId = 1L;
        Member member = createMember();
        Trip trip = Trip.builder()
            .name("test")
            .startDate(LocalDate.parse("2023-01-01"))
            .endDate(LocalDate.parse("2023-01-01"))
            .isForeign(false)
            .build();
        given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
        given(tripService.findById(tripId)).willReturn(trip);
        given(likeRepository.existsByTripAndMember(trip, member)).willReturn(true);
        likeService.deleteLike(tripId, member.getEmail());

        //then
        assertThat(trip.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 취소 실패")
    void deleteLike_fail() {

        //given
        Long tripId = 1L;
        Trip trip = createTrip();
        given(tripService.findById(tripId)).willReturn(trip);
        Member member = createMember();
        given(memberService.findMemberByEmail(member.getEmail())).willReturn(member);
        given(likeRepository.existsByTripAndMember(trip, member)).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> likeService.deleteLike(tripId, member.getEmail()))
            .isInstanceOf(InvalidLikeCancelException.class);
    }
}
