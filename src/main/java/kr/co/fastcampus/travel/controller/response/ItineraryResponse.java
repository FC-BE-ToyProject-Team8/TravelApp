package kr.co.fastcampus.travel.controller.response;

import lombok.Builder;

@Builder
public record ItineraryResponse(
    Long id,
    RouteResponse route,
    LodgeResponse lodge,
    StayResponse stay
) {

}
