package kr.co.fastcampus.travel.domain.like.repository;

import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {

    boolean existsByTripAndMember(Trip trip, Member member);

    void deleteByTripAndMember(Trip trip, Member member);

}
