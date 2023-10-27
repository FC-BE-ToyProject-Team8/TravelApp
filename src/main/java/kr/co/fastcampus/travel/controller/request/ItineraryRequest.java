package kr.co.fastcampus.travel.controller.request;

import lombok.Builder;

@Builder
public record ItineraryRequest(
    RouteRequest route,
    LodgeRequest lodge,
    StayRequest stay
) {

}
