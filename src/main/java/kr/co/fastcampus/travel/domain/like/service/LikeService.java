package kr.co.fastcampus.travel.domain.like.service;

import kr.co.fastcampus.travel.common.exception.DuplicatedLikeException;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.like.repository.LikeRepository;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final TripService tripService;

    private final LikeRepository likeRepository;

    private final MemberService memberService;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveLike(Long tripId, String memberEmail) {
        Trip trip = findTrip(tripId);
        Member member = findMember(memberEmail);
        if (isExisted(trip, member)) {
            throw new DuplicatedLikeException();
        }
        likeRepository.save(createLike(trip, member));
        trip.updateLikeCount(trip.getLikeCount() + 1);
    }

    public Trip findTrip(Long tripId) {
        return tripService.findById(tripId);
    }

    public Like createLike(Trip trip, Member member) {
        return Like.builder()
            .trip(trip)
            .member(member)
            .build();
    }

    public Member findMember(String memberEmail) {
        return memberService.findByEmail(memberEmail);
    }

    public boolean isExisted(Trip trip, Member member) {
        return likeRepository.existsByTripAndMember(trip, member);
    }
}
