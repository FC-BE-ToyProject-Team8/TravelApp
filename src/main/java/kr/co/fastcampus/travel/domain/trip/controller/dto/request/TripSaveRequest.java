package kr.co.fastcampus.travel.domain.trip.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TripSaveRequest(
    @NotBlank
    String name,
    @NotNull
    LocalDate startDate,
    @NotNull
    LocalDate endDate,
    @NotNull
    Boolean isForeign
//    @NotNull
//    Long likeCount
) {

}
