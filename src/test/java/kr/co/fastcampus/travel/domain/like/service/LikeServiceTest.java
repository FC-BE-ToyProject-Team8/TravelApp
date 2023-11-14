package kr.co.fastcampus.travel.domain.like.service;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kr.co.fastcampus.travel.common.exception.DuplicatedLikeException;
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
    void saveLike() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //given
        Long tripId = 1L;
        Trip trip = createTrip();
        given(tripService.findById(tripId)).willReturn(trip);

        //when
        for(int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try{
                    Member member = createMember();
                    likeService.saveLike(tripId, member.getEmail());
                }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        assertThat(trip.getLikeCount()).isEqualTo(threadCount);
    }

    @Test
    @DisplayName("좋아요 실패")
    void saveLike_fail() {

        //given
        Long tripId = 1L;
        Trip trip = createTrip();
        given(tripService.findById(tripId)).willReturn(trip);
        Member member = createMember();
        given(memberService.findByEmail(member.getEmail())).willReturn(member);
        given(likeRepository.existsByTripAndMember(trip, member)).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> likeService.saveLike(tripId, member.getEmail()))
            .isInstanceOf(DuplicatedLikeException.class);
    }
}
