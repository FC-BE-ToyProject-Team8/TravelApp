package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.time.LocalDate;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripSummaryResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign
) {

    public static TripSummaryResponse from(Trip trip) {
        return TripSummaryResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startDate(trip.getStartDate())
            .endDate(trip.getEndDate())
            .isForeign(trip.isForeign())
            .build();
    }
}
