package kr.co.fastcampus.travel.repository;

import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Trip, Long> {

}
