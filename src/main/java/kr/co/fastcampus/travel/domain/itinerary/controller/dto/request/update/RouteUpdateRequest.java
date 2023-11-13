package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

import java.time.LocalDateTime;

public record RouteUpdateRequest(
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

}
