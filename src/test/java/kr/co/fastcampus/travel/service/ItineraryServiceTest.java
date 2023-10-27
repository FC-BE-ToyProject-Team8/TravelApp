package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.TravelTestUtils.createItineraryRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
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
    void addItineraries() {
        // given
        Trip trip = createTrip();
        List<ItineraryRequest> requests = IntStream.range(0, 3)
            .mapToObj(i -> createItineraryRequest())
            .toList();

        //when
        Trip returnedTrip  = itineraryService.addItineraries(trip, requests);

        //then
        assertThat(returnedTrip).isNotNull();
        assertThat(returnedTrip.getItineraries().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("여정 복수 등록 실패")
    void addItineraries_fail() {
        // given
        Trip trip = createTrip();
        List<ItineraryRequest> requests = IntStream.range(0, 3)
            .mapToObj(i -> createItineraryRequest())
            .toList();
        Trip otherTrip = createTrip();

        //when
        Trip returnedTrip  = itineraryService.addItineraries(otherTrip, requests);

        //then
        assertThat(trip).isNotEqualTo(returnedTrip);
        assertThat(trip.getItineraries().size()).isNotEqualTo(returnedTrip.getItineraries().size());
    }
}
