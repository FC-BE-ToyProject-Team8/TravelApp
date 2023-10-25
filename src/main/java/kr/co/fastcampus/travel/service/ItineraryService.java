package kr.co.fastcampus.travel.service;

import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    public Itinerary findByItineraryId(Long itineraryId) {
        return itineraryRepository.findById(itineraryId)
            .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteById(Long itineraryId) {
        Itinerary itinerary = findByItineraryId(itineraryId);
        itineraryRepository.delete(itinerary);
    }
}
