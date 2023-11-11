package kr.co.fastcampus.travel.domain.trip.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TripRequest(
    @NotBlank
    String name,
    @NotNull
    LocalDate startDate,
    @NotNull
    LocalDate endDate,
    @NotNull
    Boolean isForeign
) {

}
