package kr.co.fastcampus.travel.domain.itinerary.controller.request;

import lombok.Builder;

@Builder
public record ItineraryRequest(
    RouteRequest route,
    LodgeRequest lodge,
    StayRequest stay
) {

}
