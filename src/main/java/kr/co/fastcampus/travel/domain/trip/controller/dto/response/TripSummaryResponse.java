package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.time.LocalDate;
import java.util.Objects;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripSummaryResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    int likeCount
) {

    public static TripSummaryResponse from(Trip trip) {
        if (Objects.isNull(trip)) {
            return null;
        }

        return TripSummaryResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startDate(trip.getStartDate())
            .endDate(trip.getEndDate())
            .isForeign(trip.isForeign())
            .likeCount(trip.getLikeCount())
            .build();
    }
}
