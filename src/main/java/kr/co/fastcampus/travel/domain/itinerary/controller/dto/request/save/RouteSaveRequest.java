package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

import java.time.LocalDateTime;

public record RouteSaveRequest(
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

}
