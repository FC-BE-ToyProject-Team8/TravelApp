package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

import java.util.List;

public record ItinerariesSaveRequest(
    Long tripId,
    List<ItinerarySaveRequest> itineraries
) {

}
