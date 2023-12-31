package kr.co.fastcampus.travel.domain.like.service;

import kr.co.fastcampus.travel.common.exception.DuplicatedLikeException;
import kr.co.fastcampus.travel.common.exception.InvalidLikeCancelException;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.like.repository.LikeRepository;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LikeService {

    private final TripService tripService;
    private final LikeRepository likeRepository;
    private final MemberService memberService;

    @Transactional
    public void saveLike(Long tripId, String memberEmail) {
        Trip trip = findTrip(tripId);
        Member member = findMember(memberEmail);
        if (isExisted(trip, member)) {
            throw new DuplicatedLikeException();
        }
        likeRepository.save(createLike(trip, member));
    }

    @Transactional
    public void deleteLike(Long tripId, String memberEmail) {
        Trip trip = findTrip(tripId);
        Member member = findMember(memberEmail);
        if (isExisted(trip, member)) {
            likeRepository.deleteByTripAndMember(trip, member);
        } else {
            throw new InvalidLikeCancelException();
        }
    }

    private Trip findTrip(Long tripId) {
        return tripService.findById(tripId);
    }

    private Like createLike(Trip trip, Member member) {
        return Like.builder()
            .trip(trip)
            .member(member)
            .build();
    }

    private Member findMember(String memberEmail) {
        return memberService.findMemberByEmail(memberEmail);
    }

    private boolean isExisted(Trip trip, Member member) {
        return likeRepository.existsByTripAndMember(trip, member);
    }
}
