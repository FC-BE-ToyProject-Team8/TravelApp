package kr.co.fastcampus.travel.domain.like.repository;

import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByTripAndMemberId(Trip trip, Long memberId);
}
