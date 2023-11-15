package kr.co.fastcampus.travel.domain.itinerary.service.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;
import lombok.Builder;

@Builder
public record RouteDto(
        Transportation transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

    public static RouteDto from(Route route) {
        return RouteDto.builder()
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
