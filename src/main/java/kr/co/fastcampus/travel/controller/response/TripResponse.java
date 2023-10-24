package kr.co.fastcampus.travel.controller.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TripResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign
) {
}
