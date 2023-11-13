package kr.co.fastcampus.travel.domain.trip.service;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.TravelTestUtils;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

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
                .thenReturn(Optional.of(Trip.builder().build()));

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
        Trip givenTrip = Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
                .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
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
        TripInfoDto result = tripService.editTrip(tripId, dto);

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
        assertThatThrownBy(() -> tripService.editTrip(notExistingTripId, request))
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
    @DisplayName("여정 복수 등록")
    void addItineraries() {
        // given
        Trip trip = createTrip();
        List<ItinerarySaveDto> requests = IntStream.range(0, 3)
            .mapToObj(i -> TravelTestUtils.createItinerarySaveDto())
            .toList();

        given(tripRepository.findById(trip.getId()))
                .willReturn(Optional.of(trip));

        //when
        TripItineraryInfoDto returnedTrip = tripService.addItineraries(trip.getId(), requests);

        //then
        assertThat(returnedTrip).isNotNull();
        assertThat(returnedTrip.itineraries().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("여정 복수 등록 실패")
    void addItineraries_fail() {
        // given
        Trip trip = createTrip();
        List<ItinerarySaveDto> requests = List.of();

        given(tripRepository.findById(trip.getId()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> tripService.addItineraries(trip.getId(), requests))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
