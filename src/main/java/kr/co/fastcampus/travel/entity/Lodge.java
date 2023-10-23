package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Lodge {

    @Column(length = 100)
    private String lodgePlaceName;
    private String lodgeAddress;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
}
