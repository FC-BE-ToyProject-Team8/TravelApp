package kr.co.fastcampus.travel.domain.itinerary.controller.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import lombok.Builder;

@Builder
public record RouteResponse(
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

    public static RouteResponse from(Route route) {
        return RouteResponse.builder()
                .transportation(route.getTransportation())
                .departurePlaceName(route.getDeparturePlaceName())
                .departureAddress(route.getDepartureAddress())
                .destinationPlaceName(route.getDestinationPlaceName())
                .destinationAddress(route.getDestinationAddress())
                .departureAt(route.getDepartureAt())
                .arriveAt(route.getArriveAt())
                .build();
    }
}
