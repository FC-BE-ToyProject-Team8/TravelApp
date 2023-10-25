package kr.co.fastcampus.travel.controller.request;

import lombok.Builder;

@Builder
public record ItineraryRequest (
        LodgeRequest lodge,
        RouteRequest route,
        StayRequest stay
){
}
