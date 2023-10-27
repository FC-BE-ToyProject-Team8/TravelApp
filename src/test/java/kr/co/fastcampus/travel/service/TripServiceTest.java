package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.TestUtil.createMockTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.TripRepository;
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
        assertThatThrownBy(() -> tripService.findTripById(-1L))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("여행 + 여정 조회")
    void findTripById_success() {
        // given
        when(tripRepository.findFetchItineraryById(-1L))
            .thenReturn(Optional.of(Trip.builder().build()));

        // when
        Trip result = tripService.findTripById(-1L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        Trip trip1 = createMockTrip();
        Trip trip2 = createMockTrip();
        List<Trip> trips = new ArrayList<>();
        trips.add(trip1);
        trips.add(trip2);
        given(tripRepository.findAll()).willReturn(trips);

        // when
        List<Trip> findTrips = tripService.findAllTrips();

        // then
        assertSoftly(softly -> {
            softly.assertThat(findTrips.size()).isEqualTo(2);
            softly.assertThat(findTrips).contains(trip1);
            softly.assertThat(findTrips).contains(trip2);
        });
    }

    @DisplayName("여행 수정 Service 정상 작동")
    void editTrip() {
        // given
        Long tripId = 1L;
        Trip givenTrip = Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
            .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
            .build();

        given(tripRepository.findById(tripId))
            .willReturn(Optional.of(givenTrip));
        given(tripRepository.save(any()))
            .willAnswer(invocation -> invocation.getArguments()[0]);

        TripRequest request = TripRequest.builder()
            .name("이름2")
            .startDate(LocalDate.parse("2011-01-01"))
            .endDate(LocalDate.parse("2011-01-02"))
            .isForeign(true)
            .build();

        // when
        Trip editedTrip = tripService.editTrip(tripId, request);

        // then
        assertThat(editedTrip.getName()).isEqualTo("이름2");
        assertThat(editedTrip.getStartDate().toString()).isEqualTo("2011-01-01");
        assertThat(editedTrip.getEndDate().toString()).isEqualTo("2011-01-02");
        assertThat(editedTrip.isForeign()).isEqualTo(true);
    }

    @Test
    @DisplayName("여행 수정 Service 존재하지 않는 여행 수정하려하면 예외")
    void editNotExistingTripThenThrowException() {
        // given
        Long notExistingTripId = 1L;
        given(tripRepository.findById(notExistingTripId))
            .willReturn(Optional.empty());

        TripRequest request = TripRequest.builder().build();

        // when, then
        assertThatThrownBy(() -> tripService.editTrip(notExistingTripId, request))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
