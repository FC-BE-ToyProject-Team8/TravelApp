package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import lombok.Builder;

@Builder
public record TripResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    Long likeCount,
    List<ItineraryResponse> itineraries
) {

}
