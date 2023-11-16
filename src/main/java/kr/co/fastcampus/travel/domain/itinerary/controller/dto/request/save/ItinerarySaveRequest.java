package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

public record ItinerarySaveRequest(
    RouteSaveRequest route,
    LodgeSaveRequest lodge,
    StaySaveRequest stay
) {

}
