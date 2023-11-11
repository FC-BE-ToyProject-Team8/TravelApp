package kr.co.fastcampus.travel.domain.trip.controller.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TripSummaryResponse(
    Long id,
    String name,
    LocalDate startAt,
    LocalDate endAt,
    boolean isForeign
) {

}
