package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stay {

    @Column(name = "stay_place_name", length = 100)
    private String placeName;
    @Column(name = "stay_address")
    private String address;
    private LocalDateTime startAt;
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
