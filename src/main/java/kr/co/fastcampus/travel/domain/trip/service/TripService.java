package kr.co.fastcampus.travel.domain.trip.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.MemberMismatchException;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public TripInfoDto addTrip(TripSaveDto dto, String memberEmail) {
        Member member = findMember(memberEmail);
        var trip = tripRepository.save(dto.toEntity(member));
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

    public Trip findById(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public Trip findByIdForUpdate(Long id) {
        return tripRepository.findWithOptimisticLockById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<TripInfoDto> findAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
            .map(TripInfoDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TripInfoDto editTrip(Long tripId, TripUpdateDto dto, String memberEmail) {
        var trip = findById(tripId);

        boolean isWriter = memberEmail.equals(trip.getMember().getEmail());
        if (!isWriter) {
            throw new MemberMismatchException();
        }

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
    public List<ItineraryDto> addItineraries(
        Long id, List<ItinerarySaveDto> dto, String memberEmail
    ) {
        var trip = findById(id);
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
    public Page<TripInfoDto> findTripsByNickname(String nickname, Pageable pageable) {
        Member member = findMemberByNickname(nickname);
        Page<Trip> trips = tripRepository.findTripByMember(member, pageable);
        return trips.map(TripInfoDto::from);
    }

    public Member findMemberByNickname(String nickname) {
        return memberService.findByNickname(nickname);
    }

    private Member findMember(String memberEmail) {
        return memberService.findByEmail(memberEmail);
    }

    public Page<TripInfoDto> searchByTripName(String tripName, Pageable pageable) {
        Page<Trip> trips = tripRepository.findAllByNameContainingIgnoreCase(tripName, pageable);
        return trips.map(TripInfoDto::from);
    }
}
