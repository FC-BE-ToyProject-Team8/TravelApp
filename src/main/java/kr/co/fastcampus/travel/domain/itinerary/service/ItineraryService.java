package kr.co.fastcampus.travel.domain.itinerary.service;

import static kr.co.fastcampus.travel.domain.itinerary.controller.util.ItineraryDtoConverter.toItinerary;

import java.util.List;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    @Transactional(readOnly = true)
    public Itinerary findById(Long id) {
        return itineraryRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public Itinerary editItinerary(Long id, ItineraryRequest request) {
        Itinerary itinerary = findById(id);
        Itinerary itineraryToBeUpdated = toItinerary(request);
        itinerary.update(itineraryToBeUpdated);
        return itineraryRepository.save(itinerary);
    }

    public Trip addItineraries(Trip trip, List<ItineraryRequest> request) {
        for (ItineraryRequest itineraryRequest : request) {
            Itinerary itinerary = toItinerary(itineraryRequest);
            itinerary.registerTrip(trip);
            itineraryRepository.save(itinerary);
        }
        return trip;
    }

    public void deleteById(Long id) {
        Itinerary itinerary = findById(id);
        itineraryRepository.delete(itinerary);
    }
}
