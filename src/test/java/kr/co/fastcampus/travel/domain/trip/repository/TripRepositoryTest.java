package kr.co.fastcampus.travel.domain.trip.repository;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.like.repository.LikeRepository;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LikeRepository likeRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = saveMember();
    }

    @Test
    @DisplayName("여행 + 여정 조회 검증")
    void findContainTrip() {
        // given
        Trip trip = createTrip();

        IntStream.range(0, 3)
                .forEach(i -> {
                    Itinerary itinerary = createItinerary(trip);
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
        Trip trip = createTrip();

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

    @Test
    @DisplayName("좋아요 누른 여행 목록 조회")
    void findByLike() {
        // given
        List<Trip> trips = IntStream.range(0, 2)
                .mapToObj(i -> saveTrip())
                .toList();
        trips.forEach(trip -> saveLike(member, trip));

        // when
        PageRequest request = PageRequest.of(0, 5);
        Page<Trip> result = tripRepository.findByLike(member, request);

        // then
        List<Trip> content = result.getContent();
        assertThat(content.size()).isEqualTo(2);
    }

    private Trip saveTrip() {
        Trip trip = Trip.builder()
                .name("tripName")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .isForeign(true)
                .likeCount(0L)
                .member(member)
                .build();
        tripRepository.save(trip);
        return trip;
    }

    private Member saveMember() {
        Member member = createMember();
        memberRepository.save(member);
        return member;
    }

    private void saveLike(Member member, Trip trip) {
        likeRepository.save(Like.builder()
                .member(member)
                .trip(trip)
                .build());
    }
}
