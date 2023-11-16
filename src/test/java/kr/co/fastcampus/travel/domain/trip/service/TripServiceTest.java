package kr.co.fastcampus.travel.domain.trip.service;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripWithMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.TravelTestUtils;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.InvalidDateSequenceException;
import kr.co.fastcampus.travel.common.exception.MemberMismatchException;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.LodgeSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.RouteSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.StaySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private TripService tripService;

    @Test
    @DisplayName("여행 + 여정 조회 결과 없음")
    void findTripById_failureNotFoundException() {
        // given
        when(tripRepository.findFetchItineraryById(-1L)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tripService.findTripItineraryById(-1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("여행 + 여정 조회")
    void findTripById_success() {
        // given
        when(tripRepository.findFetchItineraryById(-1L))
            .thenReturn(Optional.of(createTrip()));

        // when
        TripItineraryInfoDto result = tripService.findTripItineraryById(-1L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        Trip trip1 = createTrip();
        Trip trip2 = createTrip();
        List<Trip> trips = new ArrayList<>();
        trips.add(trip1);
        trips.add(trip2);
        given(tripRepository.findAll()).willReturn(trips);

        // when
        List<TripInfoDto> findTrips = tripService.findAllTrips();

        // then
        assertSoftly(softly -> {
            softly.assertThat(findTrips.size()).isEqualTo(2);
            softly.assertThat(findTrips).contains(TripInfoDto.from(trip1));
            softly.assertThat(findTrips).contains(TripInfoDto.from(trip2));
        });
    }

    @Test
    @DisplayName("여행 수정 Service 정상 작동")
    void editTrip() {
        // given
        Long tripId = 1L;
        Member givenMember = createMember();
        Trip givenTrip = Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
            .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
            .member(givenMember)
            .build();

        given(tripRepository.findById(tripId))
            .willReturn(Optional.of(givenTrip));

        TripUpdateDto dto = new TripUpdateDto(
            "이름2",
            LocalDate.parse("2011-01-01"),
            LocalDate.parse("2011-01-02"),
            true
        );

        // when
        TripInfoDto result = tripService.editTrip(tripId, dto, givenMember.getEmail());

        // then
        assertThat(result.name()).isEqualTo("이름2");
        assertThat(result.startDate().toString()).isEqualTo("2011-01-01");
        assertThat(result.endDate().toString()).isEqualTo("2011-01-02");
        assertThat(result.isForeign()).isEqualTo(true);
    }

    @Test
    @DisplayName("여행 수정 Service 존재하지 않는 여행 수정하려하면 예외")
    void editNotExistingTripThenThrowException() {
        // given
        Long notExistingTripId = 1L;
        given(tripRepository.findById(notExistingTripId))
            .willReturn(Optional.empty());

        TripUpdateDto request = new TripUpdateDto(
            null,
            null,
            null,
            true
        );

        // when, then
        assertThatThrownBy(() -> tripService.editTrip(notExistingTripId, request, null))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("없는 여행을 삭제하면 예외")
    void findById_failure() {
        // given
        when(tripRepository.findById(-1L))
            .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tripService.deleteTrip(-1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("여행 등록 시 종료일자가 시작일자보다 앞서면 예외")
    void addTrip_InvalidDatesequence() {
        Member member = createMember();

        // given
        TripSaveDto tripSaveDto = TripSaveDto.builder()
            .name("이름")
            .startDate(LocalDate.of(2010, 1, 2))
            .endDate(LocalDate.of(2010, 1, 1))
            .isForeign(false)
            .build();

        given(memberService.findByEmail(member.getEmail())).willReturn(member);

        // when, then
        assertThatThrownBy(() -> tripService.addTrip(tripSaveDto, member.getEmail()))
            .isInstanceOf(InvalidDateSequenceException.class);
    }

    @Test
    @DisplayName("여행 수정 시 종료일자가 시작일자보다 앞서면 예외")
    void editTrip_InvalidDatesequence() {
        // given
        Member member = createMember();

        Long tripId = 1L;
        Trip givenTrip = Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
            .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
            .member(member)
            .build();

        given(tripRepository.findById(tripId))
            .willReturn(Optional.of(givenTrip));

        TripUpdateDto dto = new TripUpdateDto(
            "이름2",
            LocalDate.parse("2011-01-02"),
            LocalDate.parse("2011-01-01"),
            true
        );

        // when, then
        assertThatThrownBy(() -> tripService.editTrip(tripId, dto, member.getEmail()))
            .isInstanceOf(InvalidDateSequenceException.class);
    }

    @Test
    @DisplayName("여행 수정 시 작성자 본인이 아니라면 예외")
    void editTrip_NoPermission() {
        // given
        Member member = createMember();

        Long tripId = 1L;
        Trip givenTrip = Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
            .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
            .member(member)
            .build();

        given(tripRepository.findById(tripId))
            .willReturn(Optional.of(givenTrip));

        TripUpdateDto dto = new TripUpdateDto(
            "이름2",
            LocalDate.parse("2011-01-02"),
            LocalDate.parse("2011-01-01"),
            true
        );

        // when, then
        assertThatThrownBy(() -> tripService.editTrip(tripId, dto, "another@email.com"))
            .isInstanceOf(MemberMismatchException.class);
    }

    @Test
    @DisplayName("사용자 닉네임으로 여행 검색")
    void findTripsByNickname() {
        //given
        Member member = createMember();
        String query = member.getNickname();
        Pageable pageable = PageRequest.of(0, 3);
        List<Trip> trips = IntStream.range(0, 10)
            .mapToObj(i -> createTripWithMember(member))
            .toList();
        List<TripInfoDto> tripInfos = trips.stream()
            .map(TripInfoDto::from)
            .toList();

        when(tripRepository.findAllByNameContainingIgnoreCase(query, pageable))
            .thenReturn(new PageImpl<>(trips, pageable, trips.size()));

        // When
        Page<TripInfoDto> result = tripService.searchByTripName(query, pageable);

        // Then
        assertSoftly(softly -> {
            softly.assertThat(result.getNumber()).isEqualTo(0);
            softly.assertThat(result.getSize()).isEqualTo(3);
            softly.assertThat(result.getTotalElements()).isEqualTo(tripInfos.size());
            softly.assertThat(result.getContent()).isEqualTo(tripInfos);
        });
    }

    @Test
    @DisplayName("사용자 닉네임으로 여행 검색 실패")
    void findTripsByNickname_fail() {
        //given
        String query = "findTripsByNickname_fail";
        Pageable pageable = PageRequest.of(0, 3);
        when(tripRepository.findAllByNameContainingIgnoreCase(query, pageable))
            .thenReturn(Page.empty());

        // when
        Page<TripInfoDto> actualResult = tripService.searchByTripName(query, pageable);

        // then
        assertThat(actualResult.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("여행 이름으로 여행 검색 - 성공 케이스")
    void searchByTripName() {
        String query = "trip";
        Pageable pageable = PageRequest.of(0, 3);
        List<Trip> trips = IntStream.range(0, 10)
            .mapToObj(i -> createTrip())
            .toList();
        List<TripInfoDto> tripInfos = trips.stream()
            .map(TripInfoDto::from)
            .toList();

        when(tripRepository.findAllByNameContainingIgnoreCase(query, pageable))
            .thenReturn(new PageImpl<>(trips, pageable, trips.size()));

        // When
        Page<TripInfoDto> result = tripService.searchByTripName(query, pageable);

        // Then
        assertSoftly(softly -> {
            softly.assertThat(result.getNumber()).isEqualTo(0);
            softly.assertThat(result.getSize()).isEqualTo(3);
            softly.assertThat(result.getTotalElements()).isEqualTo(tripInfos.size());
            softly.assertThat(result.getContent()).isEqualTo(tripInfos);
        });
    }

    @Test
    @DisplayName("여행 이름으로 여행 검색 결과 X")
    void searchByTripNameNoResult() {
        // Given
        String query = "no result";
        Pageable pageable = PageRequest.of(0, 3);
        when(tripRepository.findAllByNameContainingIgnoreCase(query, pageable))
            .thenReturn(Page.empty());

        // When
        Page<TripInfoDto> actualResult = tripService.searchByTripName(query, pageable);

        // Then
        assertThat(actualResult.isEmpty()).isEqualTo(true);
    }
}
