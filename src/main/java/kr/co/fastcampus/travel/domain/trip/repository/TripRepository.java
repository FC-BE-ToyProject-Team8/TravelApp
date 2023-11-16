package kr.co.fastcampus.travel.domain.trip.repository;

import java.util.List;
import java.util.Optional;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends CrudRepository<Trip, Long> {

    @Query(
        "SELECT t "
            + "FROM Trip t "
            + "LEFT JOIN FETCH t.itineraries i "
            + "WHERE t.id = :id"
    )
    Optional<Trip> findFetchItineraryById(@Param("id") Long id);

    @Override
    List<Trip> findAll();

    Page<Trip> findTripByMember(Member member, Pageable pageable);

    Page<Trip> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(
        "SELECT DISTINCT t FROM Trip t "
                + "LEFT JOIN FETCH Like l ON t = l.trip "
                + "WHERE l.member = :member"
    )
    Page<Trip> findByLike(@Param("member") Member member, Pageable pageable);
}
