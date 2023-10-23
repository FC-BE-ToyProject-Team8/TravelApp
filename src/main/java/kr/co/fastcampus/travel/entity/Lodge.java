package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Lodge {

    @Column(name = "lodge_place_name", length = 100)
    private String placeName;
    @Column(name = "lodge_address")
    private String address;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
}
