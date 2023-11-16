package kr.co.fastcampus.travel.domain.itinerary.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.MemberMismatchException;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.ItineraryUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
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
    private final TripService tripService;

    @Transactional
    public List<ItineraryDto> addItineraries(
        Long id, List<ItinerarySaveDto> dto, String memberEmail
    ) {
        var trip = tripService.findById(id);
        if (trip.getMember().getEmail().equals(memberEmail)) {
            dto.stream()
                .map(itinerarySaveDto -> itinerarySaveDto.toEntity(trip))
                .forEach(trip::addItinerary);
            return trip.getItineraries().stream()
                .map(ItineraryDto::from)
                .collect(Collectors.toList());
        } else {
            throw new MemberMismatchException();
        }
    }

    @Transactional
    public ItineraryDto editItinerary(Long id, String memberEmail, ItineraryUpdateDto dto) {
        var itinerary = findById(id);
        validateMemberMatch(memberEmail, itinerary);
        Itinerary updateItinerary = dto.toEntity();
        itinerary.update(updateItinerary);
        return ItineraryDto.from(itinerary);
    }

    public void deleteById(Long id, String email) {
        var itinerary = findById(id);
        String writerEmail = itinerary.getTrip().getMember().getEmail();
        if (!writerEmail.equals(email)) {
            throw new MemberMismatchException();
        }
        itineraryRepository.delete(itinerary);
    }

    private Itinerary findById(Long id) {
        return itineraryRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    private void validateMemberMatch(String memberEmail, Itinerary itinerary) {
        if (!itinerary.getTrip().getMember().getEmail().equals(memberEmail)) {
            throw new MemberMismatchException();
        }
    }
}
