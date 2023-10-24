package kr.co.fastcampus.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.TripUpdateRequest;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TravelServiceTest {

    @InjectMocks
    private TravelService travelService;

    @Mock
    private TripRepository tripRepository;

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
        given(tripRepository.save(any()))
            .willAnswer(invocation -> invocation.getArguments()[0]);

        TripUpdateRequest request = TripUpdateRequest.builder()
            .name("이름2").startDate("2011-01-01").endDate("2011-01-02").isForeign(true)
            .build();

        // when
        Trip editedTrip = travelService.editTrip(tripId, request);

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

        TripUpdateRequest request = TripUpdateRequest.builder().build();

        // when, then
        assertThatThrownBy(() -> travelService.editTrip(notExistingTripId, request))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
