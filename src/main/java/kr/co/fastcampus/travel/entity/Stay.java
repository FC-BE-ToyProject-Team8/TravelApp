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
public class Stay {

    @Column(name = "stay_place_name", length = 100)
    private String placeName;
    @Column(name = "stay_address")
    private String address;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
