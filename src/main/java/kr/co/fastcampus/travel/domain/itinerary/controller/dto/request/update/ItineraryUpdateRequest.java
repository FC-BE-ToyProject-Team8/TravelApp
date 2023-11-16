package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

public record ItineraryUpdateRequest(
    RouteUpdateRequest route,
    LodgeUpdateRequest lodge,
    StayUpdateRequest stay
) {

}
