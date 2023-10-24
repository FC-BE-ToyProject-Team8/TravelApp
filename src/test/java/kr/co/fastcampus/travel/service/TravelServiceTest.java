package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.TravelUtils.createTrip;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @InjectMocks
    private TravelService travelService;

    @Mock
    private TripRepository tripRepository;

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
        List<Trip> findTrips = travelService.findAllTrips();

        // then
        assertSoftly(softly -> {
            softly.assertThat(findTrips.size()).isEqualTo(2);
            softly.assertThat(findTrips).contains(trip1);
            softly.assertThat(findTrips).contains(trip2);
        });
    }
}
