package kr.co.fastcampus.travel.domain.trip.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kr.co.fastcampus.travel.common.baseentity.BaseEntity;
import kr.co.fastcampus.travel.common.exception.InvalidDateSequenceException;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Trip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isForeign;

    @Column(nullable = false)
    private Long likeCount;

    @OneToMany(
        fetch = FetchType.LAZY, mappedBy = "trip",
        cascade = CascadeType.PERSIST, orphanRemoval = true
    )
    private List<Itinerary> itineraries = new ArrayList<>();

    @Version
    private Long version;

    @Builder
    private Trip(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean isForeign,
        Long likeCount,
        Member member
    ) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateSequenceException();
        }
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isForeign = isForeign;
        this.likeCount = likeCount;
        this.member = member;
    }

    public void update(Trip tripToBeUpdated) {
        this.name = tripToBeUpdated.getName();
        this.startDate = tripToBeUpdated.getStartDate();
        this.endDate = tripToBeUpdated.getEndDate();
        this.isForeign = tripToBeUpdated.isForeign();
    }

    public void updateLikeCount(Long changedLikeCount) {
        this.likeCount = changedLikeCount;
    }

    public void addItinerary(Itinerary itinerary) {
        itineraries.add(itinerary);
    }
}
