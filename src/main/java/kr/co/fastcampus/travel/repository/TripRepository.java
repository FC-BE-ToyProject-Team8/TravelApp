package kr.co.fastcampus.travel.repository;

import kr.co.fastcampus.travel.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

}
