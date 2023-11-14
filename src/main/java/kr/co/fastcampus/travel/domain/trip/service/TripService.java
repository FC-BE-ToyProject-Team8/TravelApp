package kr.co.fastcampus.travel.domain.trip.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.MemberNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripRepository tripRepository;

    private final MemberService memberService;

    private final int pageSize = 5;

    @Transactional
    public TripInfoDto addTrip(TripSaveDto dto) {
        var trip = tripRepository.save(dto.toEntity());
        return TripInfoDto.from(trip);
    }

    public TripItineraryInfoDto findTripItineraryById(Long id) {
        var trip = tripRepository.findFetchItineraryById(id)
            .orElseThrow(EntityNotFoundException::new);
        return TripItineraryInfoDto.from(trip);
    }

    public TripInfoDto findTripById(Long id) {
        var trip = findById(id);
        return TripInfoDto.from(trip);
    }

    private Trip findById(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<TripInfoDto> findAllTrips() {
        var trips = tripRepository.findAll();
        return trips.stream()
            .map(TripInfoDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TripInfoDto editTrip(Long tripId, TripUpdateDto dto) {
        var trip = findById(tripId);
        Trip updateTrip = dto.toEntity();
        trip.update(updateTrip);
        return TripInfoDto.from(trip);
    }

    @Transactional
    public void deleteTrip(Long id) {
        var trip = findById(id);
        tripRepository.delete(trip);
    }

    @Transactional
    public List<ItineraryDto> addItineraries(Long id, List<ItinerarySaveDto> dto) {
        var trip = findById(id);
        dto.stream()
                .map(ItinerarySaveDto::toEntity)
                .forEach(trip::addItinerary);
        return trip.getItineraries().stream()
            .map(ItineraryDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public List<TripInfoDto> findTripsByNickname(String nickname, int page, Pageable pageable) {
        Member member = findMemberByNickname(nickname);
        pageable = PageRequest.of(page - 1, pageSize);
        var trips = tripRepository.findTripByMember(member, pageable);
        return trips.stream()
            .map(TripInfoDto::from)
            .collect(Collectors.toList());
    }

    public Member findMemberByNickname(String nickname) {
        return memberService.findByNickname(nickname);
    }
}
