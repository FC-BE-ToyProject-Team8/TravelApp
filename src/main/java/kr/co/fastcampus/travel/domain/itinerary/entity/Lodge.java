package kr.co.fastcampus.travel.domain.itinerary.entity;

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
public class Lodge {

    @Column(name = "lodge_place_name", length = 100)
    private String placeName;

    @Column(name = "lodge_address", length = 200)
    private String address;

    private LocalDateTime checkInAt;

    private LocalDateTime checkOutAt;

    @Builder
    private Lodge(
        String placeName,
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
    ) {
        this.placeName = placeName;
        this.address = address;
        this.checkInAt = checkInAt;
        this.checkOutAt = checkOutAt;
    }
}
