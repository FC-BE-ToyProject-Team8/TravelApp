package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Route {

    @Column(length = 100)

    private String transportation;
    @Column(length = 100)

    private String departurePlaceName;
    private String departureAddress;
    @Column(length = 100)

    private String destinationPlaceName;
    private String destinationAddress;
    private LocalDateTime departureAt;
    private LocalDateTime arriveAt;
}
