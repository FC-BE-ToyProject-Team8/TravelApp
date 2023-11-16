package kr.co.fastcampus.travel.domain.like.service.dto;

import kr.co.fastcampus.travel.domain.like.entity.Like;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record LikeSaveDto(
    Member member,
    Trip trip
) {
    public Like toEntity() {
        return Like.builder()
            .member(member)
            .trip(trip)
            .build();
    }
}