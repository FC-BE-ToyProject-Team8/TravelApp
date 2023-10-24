package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
    private String departureAddress;
    @Column(length = 100)

    private String destinationPlaceName;
    private String destinationAddress;
    private LocalDateTime departureAt;
    private LocalDateTime arriveAt;
}
