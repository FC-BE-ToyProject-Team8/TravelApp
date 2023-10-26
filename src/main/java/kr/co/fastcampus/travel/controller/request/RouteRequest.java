package kr.co.fastcampus.travel.controller.request;

import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RouteRequest(
    String transportation,
    String departurePlaceName,
    String departureAddress,
    String destinationPlaceName,
    String destinationAddress,
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})$", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
    LocalDateTime departureAt,
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})$", message = "날짜 형식은 yyyy-MM-dd 여야 합니다.")
    LocalDateTime arriveAt
) {

}
