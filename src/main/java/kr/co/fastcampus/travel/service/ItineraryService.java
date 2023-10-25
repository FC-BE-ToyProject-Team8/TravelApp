package kr.co.fastcampus.travel.service;

import jakarta.transaction.Transactional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import kr.co.fastcampus.travel.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toItinerary;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    private final TripRepository tripRepository;

    @Transactional
    public Trip addItineraries(Long id, List<ItineraryRequest> request) {
        Trip trip = tripRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        for (ItineraryRequest itineraryRequest : request){
            Itinerary itinerary = toItinerary(itineraryRequest);
            itinerary.registerTrip(trip);
            itineraryRepository.save(itinerary);
        }
        return tripRepository.findFetchItineraryById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
