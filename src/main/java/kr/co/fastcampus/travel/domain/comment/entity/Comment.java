package kr.co.fastcampus.travel.domain.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.fastcampus.travel.common.baseentity.BaseEntity;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(nullable = false, length = 1000)
    private String content;

    @Builder
    private Comment(
        Long id,
        Member member,
        Trip trip,
        String content
    ) {
        this.id = id;
        this.member = member;
        this.trip = trip;
        this.content = content;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void update(String commentToBeUpdated) {
        this.content = commentToBeUpdated;
    }
}
