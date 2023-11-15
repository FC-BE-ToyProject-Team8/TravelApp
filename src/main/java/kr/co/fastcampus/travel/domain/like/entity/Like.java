package kr.co.fastcampus.travel.domain.like.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.fastcampus.travel.common.baseentity.BaseEntity;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like_trip")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Builder
    public Like(Member member, Trip trip) {
        this.member = member;
        this.trip = trip;
    }
}
