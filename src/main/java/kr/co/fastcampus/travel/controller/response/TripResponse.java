package kr.co.fastcampus.travel.controller.response;

import lombok.Builder;

@Builder
public record TripResponse(
    Long id,
    String name,
    String startDate,
    String endDate,
    boolean isForeign
) {

}
