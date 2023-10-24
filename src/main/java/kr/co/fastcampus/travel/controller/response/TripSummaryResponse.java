package kr.co.fastcampus.travel.controller.response;

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
