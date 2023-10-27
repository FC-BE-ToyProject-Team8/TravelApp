package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toItinerary;

import java.util.List;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
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
