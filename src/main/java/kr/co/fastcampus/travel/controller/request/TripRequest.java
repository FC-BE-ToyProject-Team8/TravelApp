package kr.co.fastcampus.travel.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record TripRequest(
    @NotBlank
    String name,
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})$", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
    String startDate,
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})$", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
    String endDate,
    @Pattern(regexp = "true|false", message = "값은 true 또는 false 여야 합니다.")
    boolean isForeign
) {

}
