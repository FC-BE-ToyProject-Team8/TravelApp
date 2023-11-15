package kr.co.fastcampus.travel.domain.trip.repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends CrudRepository<Trip, Long> {

    @Query(
        "select t "
            + "from Trip t "
            + "left join fetch t.itineraries i "
            + "where t.id = :id"
    )
    Optional<Trip> findFetchItineraryById(@Param("id") Long id);

    @Override
    List<Trip> findAll();

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Trip> findWithOptimisticLockById(Long id);

    Page<Trip> findTripByMember(Member member, Pageable pageable);
}
