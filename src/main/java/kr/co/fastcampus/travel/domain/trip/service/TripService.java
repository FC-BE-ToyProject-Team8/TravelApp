package kr.co.fastcampus.travel.domain.trip.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.MemberMismatchException;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripDetailDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
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

    public List<TripInfoDto> findAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
            .map(TripInfoDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TripInfoDto addTrip(TripSaveDto dto, String memberEmail) {
        Member member = findMember(memberEmail);
        var trip = tripRepository.save(dto.toEntity(member));
        return TripInfoDto.from(trip);
    }

    private Member findMember(String memberEmail) {
        return memberService.findMemberByEmail(memberEmail);
    }

    public TripDetailDto findTripDetailById(Long id) {
        var trip = tripRepository.findFetchDetailById(id)
            .orElseThrow(EntityNotFoundException::new);
        return TripDetailDto.from(trip);
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

    public Page<TripInfoDto> findTripsByNickname(String nickname, Pageable pageable) {
        Member member = findMemberByNickname(nickname);
        Page<Trip> trips = tripRepository.findTripByMember(member, pageable);
        return trips.map(TripInfoDto::from);
    }

    private Member findMemberByNickname(String nickname) {
        return memberService.findByNickname(nickname);
    }

    public Page<TripInfoDto> searchByTripName(String tripName, Pageable pageable) {
        Page<Trip> trips = tripRepository.findAllByNameContainingIgnoreCase(tripName, pageable);
        return trips.map(TripInfoDto::from);
    }

    public List<TripInfoDto> findTripsByLike(String email, Pageable pageable) {
        var member = memberService.findMemberByEmail(email);
        var trips = tripRepository.findByLike(member, pageable);
        return trips.stream()
                .map(TripInfoDto::from)
                .collect(Collectors.toList());
    }

    public Trip findById(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }
}
