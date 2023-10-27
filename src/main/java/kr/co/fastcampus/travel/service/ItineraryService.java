package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toItinerary;

import jakarta.transaction.Transactional;
import java.util.List;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    @Transactional
    public Long addItineraries(Trip trip, List<ItineraryRequest> request) {
        for (ItineraryRequest itineraryRequest : request) {
            Itinerary itinerary = toItinerary(itineraryRequest);
            itinerary.registerTrip(trip);
            itineraryRepository.save(itinerary);
        }
        return trip.getId();
    }
}
