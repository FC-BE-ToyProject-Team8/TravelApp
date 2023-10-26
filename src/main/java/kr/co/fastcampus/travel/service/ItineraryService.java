package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toItinerary;

import jakarta.transaction.Transactional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    public Itinerary findByItineraryId(Long itineraryId) {
        return itineraryRepository.findById(itineraryId)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Itinerary editItinerary(Long id, ItineraryRequest request) {
        Itinerary itinerary = findByItineraryId(id);
        Itinerary itineraryToBeUpdated = toItinerary(request);
        itinerary.update(itineraryToBeUpdated);
        return itineraryRepository.save(itinerary);
    }
}
