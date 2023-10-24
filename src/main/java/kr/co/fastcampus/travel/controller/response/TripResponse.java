package kr.co.fastcampus.travel.controller.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record TripResponse(
        Long id,
        String name,
        LocalDate startAt,
        LocalDate endAt,
        List<ItineraryResponse> itineraries
) {

}
