package kr.co.fastcampus.travel.controller.request;


import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RouteRequest(
    String transportation,
    String departurePlaceName,
    String departureAddress,
    String destinationPlaceName,
    String destinationAddress,

    LocalDateTime departureAt,

    LocalDateTime arriveAt
) {

}
