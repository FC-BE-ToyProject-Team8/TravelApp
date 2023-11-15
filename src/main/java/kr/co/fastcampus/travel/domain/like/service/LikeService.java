package kr.co.fastcampus.travel.domain.like.service;

import jakarta.transaction.Transactional;
import kr.co.fastcampus.travel.common.exception.DuplicatedLikeException;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.like.repository.LikeRepository;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final TripRepository tripRepository;

    private final LikeRepository likeRepository;

    private final MemberRepository memberRepository;

    public void saveLike(Long tripId, Long memberId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(EntityNotFoundException::new);
        if (likeRepository.existsByTripAndMemberId(trip, memberId)) {
            throw new DuplicatedLikeException();
        }
        Like like = Like.builder()
            .trip(trip)
            .member(memberRepository.findById(memberId).get())
            .build();
        likeRepository.save(like);
        updateLikeCount(trip, 1);
    }

    public void updateLikeCount(Trip trip, int plusNumber){

    }
}
