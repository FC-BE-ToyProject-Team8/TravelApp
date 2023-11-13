package kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;

public record RouteSaveDto(
        String transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

    public Route toEntity() {
        return Route.builder()
                .transportation(transportation())
                .departurePlaceName(departurePlaceName())
                .departureAddress(departureAddress())
                .destinationPlaceName(destinationPlaceName())
                .destinationAddress(destinationAddress())
                .departureAt(departureAt())
                .arriveAt(arriveAt())
                .build();
    }
}
