package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TripSummaryResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign
) {

}
