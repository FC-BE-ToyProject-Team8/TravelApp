package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Itinerary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Route route;

    @Embedded
    private Lodge lodge;

    @Embedded
    private Stay stay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Builder
    private Itinerary(Route route, Lodge lodge, Stay stay) {
        this.route = route;
        this.lodge = lodge;
        this.stay = stay;
    }

    @Builder
    private Itinerary(
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt,
        String lodgePlaceName,
        String lodgeAddress,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        String stayPlaceName,
        String stayAddress,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        this.route = Route.builder()
            .transportation(transportation)
            .departurePlaceName(departurePlaceName)
            .departureAddress(departureAddress)
            .destinationPlaceName(destinationPlaceName)
            .destinationAddress(destinationAddress)
            .departureAt(departureAt)
            .arriveAt(arriveAt)
            .build();

        this.lodge = Lodge.builder()
            .placeName(lodgePlaceName)
            .address(lodgeAddress)
            .checkInAt(checkInAt)
            .checkOutAt(checkOutAt)
            .build();

        this.stay = Stay.builder()
            .placeName(stayPlaceName)
            .address(stayAddress)
            .startAt(startAt)
            .endAt(endAt)
            .build();
    }

    public void update(Itinerary itineraryToBeUpdated) {
        this.route = itineraryToBeUpdated.getRoute();
        this.lodge = itineraryToBeUpdated.getLodge();
        this.stay = itineraryToBeUpdated.getStay();
    }

    public void registerTrip(Trip trip) {
        if (this.trip != null) {
            this.trip.getItineraries().remove(this);
        }
        trip.addItinerary(this);
        this.trip = trip;
    }
}
