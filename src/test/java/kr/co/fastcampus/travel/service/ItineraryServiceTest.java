package kr.co.fastcampus.travel.service;

import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static kr.co.fastcampus.travel.testUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItineraryServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    @Test
    @DisplayName("여정 복수 등록")
    void addItineraries(){
        // given
        Trip trip = createMockTrip();
        Long id = -1L;
        given(tripRepository.findById(id)).willReturn(Optional.of(trip));
        given(tripRepository.findFetchItineraryById(id)).willReturn(Optional.of(trip));
        List<ItineraryRequest> requests = IntStream.range(0, 3)
                .mapToObj(i -> createMockItineraryRequest())
                .toList();
//
        //when
        Trip foundTripWithItineraries = itineraryService.addItineraries(id,requests);

        //then
        assertThat(foundTripWithItineraries).isNotNull();
        assertThat(foundTripWithItineraries.getItineraries().size()).isEqualTo(requests.size());
    }

    @Test
    @DisplayName("여정 복수 등록 실패")
    void addItineraries_fail(){
        // given
        Trip trip = createMockTrip();
        when(tripRepository.findById(-1L)).thenReturn(Optional.empty());
        List<ItineraryRequest> requests = IntStream.range(0, 3)
                .mapToObj(i -> createMockItineraryRequest())
                .toList();
//
        //when

        //then
        assertThatThrownBy(() -> itineraryService.addItineraries(-1L,requests))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
