package kr.co.fastcampus.travel.controller.response;

import java.time.LocalDate;

public record TripResponse(
    Long id,
    String name,
    String startDate,
    String endDate,
    boolean isForeign
) {

}
