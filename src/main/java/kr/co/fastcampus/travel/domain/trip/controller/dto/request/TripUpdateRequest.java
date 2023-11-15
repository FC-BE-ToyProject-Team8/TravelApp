package kr.co.fastcampus.travel.domain.trip.controller.dto.request;

import java.time.LocalDate;

public record TripUpdateRequest(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    Boolean isForeign,
    Long likeCount
) {

}
