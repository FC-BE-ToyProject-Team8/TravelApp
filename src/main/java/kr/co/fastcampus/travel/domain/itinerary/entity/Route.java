package kr.co.fastcampus.travel.domain.itinerary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import kr.co.fastcampus.travel.common.exception.InvalidDateSequenceException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {

    @Enumerated(EnumType.STRING)
    private Transportation transportation;

    @Column(length = 100)
    private String departurePlaceName;

    @Column(length = 200)
    private String departureAddress;

    @Column(length = 100)
    private String destinationPlaceName;

    @Column(length = 200)
    private String destinationAddress;

    private LocalDateTime departureAt;

    private LocalDateTime arriveAt;

    @Builder
    private Route(
        Transportation transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
    ) {
        if (arriveAt.isBefore(departureAt)) {
            throw new InvalidDateSequenceException();
        }

        this.transportation = transportation;
        this.departurePlaceName = departurePlaceName;
        this.departureAddress = departureAddress;
        this.destinationPlaceName = destinationPlaceName;
        this.destinationAddress = destinationAddress;
        this.departureAt = departureAt;
        this.arriveAt = arriveAt;
    }
}
