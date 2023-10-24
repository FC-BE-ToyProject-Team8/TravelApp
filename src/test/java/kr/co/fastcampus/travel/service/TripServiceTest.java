package kr.co.fastcampus.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    private TripService tripService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        tripService = new TripService(tripRepository);
    }

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
        when(tripRepository.findFetchItineraryById(-1L)).thenReturn(Optional.of(Trip.builder().build()));

        // when
        Trip result = tripService.findTripById(-1L);

        // then
        assertThat(result).isNotNull();
    }
}
