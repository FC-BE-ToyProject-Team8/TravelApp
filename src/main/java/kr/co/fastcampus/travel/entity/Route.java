package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Route {

    @Column(length = 100)
    private String transportation;

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
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
    ) {
        this.transportation = transportation;
        this.departurePlaceName = departurePlaceName;
        this.departureAddress = departureAddress;
        this.destinationPlaceName = destinationPlaceName;
        this.destinationAddress = destinationAddress;
        this.departureAt = departureAt;
        this.arriveAt = arriveAt;
    }
}
