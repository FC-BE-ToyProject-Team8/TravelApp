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
public class Stay {

    @Column(name = "stay_place_name", length = 100)
    private String placeName;

    @Column(name = "stay_address", length = 200)
    private String address;

    @Column(name = "stay_start_at")
    private LocalDateTime startAt;

    @Column(name = "stay_end_at")
    private LocalDateTime endAt;

    @Builder
    private Stay(
        String placeName,
        String address,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {
        this.placeName = placeName;
        this.address = address;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
