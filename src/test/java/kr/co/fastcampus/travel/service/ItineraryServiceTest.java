package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.TravelTestUtils.createMockItinerary;
import static kr.co.fastcampus.travel.TravelTestUtils.createMockTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    @Test
    @DisplayName("여정 삭제")
    void deleteItinerary() {
        Trip trip = createMockTrip();
        Itinerary itinerary1 = createMockItinerary(trip);
        Itinerary itinerary2 = createMockItinerary(trip);


        when(itineraryRepository.findById(any())).thenReturn(Optional.of(itinerary2));

        //when
        itineraryService.deleteById(itinerary2.getId());

        //then
        verify(itineraryRepository).delete(itinerary2);
    }

    @Test
    @DisplayName("존재하지 않는 여정 삭제")
    void deleteNoneItinerary() {
        // given
        when(itineraryRepository.findById(any())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException e =
            assertThrows(EntityNotFoundException.class, () -> itineraryService.deleteById(2L));

        // then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 엔티티입니다.");
    }

}
