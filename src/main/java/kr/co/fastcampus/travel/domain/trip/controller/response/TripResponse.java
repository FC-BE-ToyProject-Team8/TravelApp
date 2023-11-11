package kr.co.fastcampus.travel.domain.trip.controller.response;

import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.ItineraryResponse;
import lombok.Builder;

@Builder
public record TripResponse(
    Long id,
    String name,
    LocalDate startAt,
    LocalDate endAt,
    boolean isForeign,
    List<ItineraryResponse> itineraries
) {

}
