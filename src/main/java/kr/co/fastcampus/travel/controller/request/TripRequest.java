package kr.co.fastcampus.travel.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

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
