package kr.co.fastcampus.travel.repository;

import static kr.co.fastcampus.travel.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Test
    @DisplayName("여행 + 여정 조회 검증")
    void findContainTrip() {
        // given
        Trip trip = Trip.builder()
            .name("여행")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .build();

        IntStream.range(0, 3)
            .forEach(i -> {
                Itinerary itinerary = Itinerary.builder().build();
                itinerary.registerTrip(trip);
            });

        tripRepository.save(trip);

        // when
        Trip result = tripRepository.findFetchItineraryById(trip.getId()).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trip.getId());
        assertThat(result.getItineraries().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("여정 없는 여행 조회 검증")
    void findOnlyTrip() {
        // given
        Trip trip = Trip.builder()
            .name("여행")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(7))
            .build();

        tripRepository.save(trip);

        // when
        Trip result = tripRepository.findFetchItineraryById(trip.getId()).orElse(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(trip.getId());
        assertThat(result.getItineraries().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("여행 목록 조회")
    void findAll() {
        // given
        List<Trip> saveTrips = IntStream.range(0, 2)
            .mapToObj(i -> saveTrip())
            .toList();

        // when
        List<Trip> trips = tripRepository.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(trips.size()).isEqualTo(2);
            softly.assertThat(trips).contains(saveTrips.get(0));
            softly.assertThat(trips).contains(saveTrips.get(1));
        });
    }

    private Trip saveTrip() {
        Trip trip = createTrip();
        tripRepository.save(trip);
        return trip;
    }
}
