package kr.co.fastcampus.travel.domain.trip.service.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripItineraryInfoDto(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    Long likeCount,
    List<ItineraryResponse> itineraries
) {

    public static TripItineraryInfoDto from(Trip trip) {
        return TripItineraryInfoDto.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startDate(trip.getStartDate())
            .endDate(trip.getEndDate())
            .isForeign(trip.isForeign())
            .likeCount(trip.getLikeCount())
            .itineraries(getItineraries(trip))
            .build();
    }

    private static List<ItineraryResponse> getItineraries(Trip trip) {
        return trip.getItineraries().stream()
            .map(ItineraryResponse::from)
            .collect(Collectors.toList());
    }
}
