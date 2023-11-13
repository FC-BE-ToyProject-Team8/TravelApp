package kr.co.fastcampus.travel.domain.itinerary.service;

import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.ItineraryUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
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
    public ItineraryDto findItineraryById(Long id) {
        var itinerary = findById(id);
        return ItineraryDto.from(itinerary);
    }

    public ItineraryDto editItinerary(Long id, ItineraryUpdateDto dto) {
        var itinerary = findById(id);
        Itinerary updateItinerary = dto.toEntity();
        itinerary.update(updateItinerary);
        return ItineraryDto.from(itinerary);
    }

    public void deleteById(Long id) {
        var itinerary = findById(id);
        itineraryRepository.delete(itinerary);
    }

    private Itinerary findById(Long id) {
        return itineraryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
