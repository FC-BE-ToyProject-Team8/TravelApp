package kr.co.fastcampus.travel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Itinerary {

    @Id
    @Column(name = "itinerary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @ManyToOne
    private Trip trip;

    @Column(length = 100)
    private String movePlaceName;

    @Column(length = 100)
    private String moveAddress;

    @Column(length = 100)
    private String lodgePlaceName;

    @Column(length = 100)
    private String lodgeAddress;

    @Column(length = 100)
    private String stayPlaceName;

    @Column(length = 100)
    private String stayAddress;
}
