package kr.co.fastcampus.travel.repository;

import java.util.Optional;
import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends CrudRepository<Trip, Long> {
    @Query(
            "select t "
                    + "from Trip t "
                    + "join fetch t.itineraries i "
                    + "where t.id = :id"
    )
    Optional<Trip> findFetchItineraryById(@Param("id") Long id);
}
