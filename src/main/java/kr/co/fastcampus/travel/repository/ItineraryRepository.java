package kr.co.fastcampus.travel.repository;

import kr.co.fastcampus.travel.entity.Itinerary;
import org.springframework.data.repository.CrudRepository;

public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {
}
