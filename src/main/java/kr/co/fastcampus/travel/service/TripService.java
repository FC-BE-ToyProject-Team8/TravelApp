package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTrip;

import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripRepository tripRepository;

    public Trip addTrip(TripRequest request) {
        Trip trip = toTrip(request);
        return tripRepository.save(trip);
    }

    public Trip findTripById(Long id) {
        return tripRepository.findFetchItineraryById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
