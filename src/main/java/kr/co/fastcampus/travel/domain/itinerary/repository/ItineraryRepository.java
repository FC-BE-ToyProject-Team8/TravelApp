package kr.co.fastcampus.travel.domain.itinerary.repository;

import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import org.springframework.data.repository.CrudRepository;

public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

}
