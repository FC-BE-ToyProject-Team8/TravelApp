package kr.co.fastcampus.travel.domain.trip.service.dto.response;

import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.itinerary.controller.util.ItineraryDtoConverter;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripItineraryInfoDto(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    List<ItineraryResponse> itineraries
) {

    public static TripItineraryInfoDto from(Trip trip) {
        return TripItineraryInfoDto.builder()
                .id(trip.getId())
                .name(trip.getName())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .isForeign(trip.isForeign())
                .itineraries(ItineraryDtoConverter.getItineraryResponses(trip))
                .build();
    }
}
