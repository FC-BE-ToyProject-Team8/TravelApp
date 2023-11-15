package kr.co.fastcampus.travel.domain.trip.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TripUpdateRequest(
    @NotBlank(message = "name은 필수로 입력하셔야 합니다.")
    String name,
    @NotNull(message = "startDate는 필수로 입력하셔야 합니다.")
    LocalDate startDate,
    @NotNull(message = "endDate는 필수로 입력하셔야 합니다.")
    LocalDate endDate,
    @NotNull(message = "isForeign은 필수로 입력하셔야 합니다.")
    Boolean isForeign,
    Long likeCount
) {

}
