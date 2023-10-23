package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class Stay {

    @Column(length = 100)
    private String stayPlaceName;
    private String stayAddress;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
