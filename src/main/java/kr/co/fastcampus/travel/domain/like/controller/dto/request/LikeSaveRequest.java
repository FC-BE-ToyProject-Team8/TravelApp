package kr.co.fastcampus.travel.domain.like.controller.dto.request;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;

public record LikeSaveRequest(
    Long id,
    Member member,
    Trip trip
) {

}
