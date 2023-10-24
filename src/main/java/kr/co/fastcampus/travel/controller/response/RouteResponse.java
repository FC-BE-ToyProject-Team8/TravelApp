package kr.co.fastcampus.travel.controller.response;

import java.time.LocalDateTime;
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

}
