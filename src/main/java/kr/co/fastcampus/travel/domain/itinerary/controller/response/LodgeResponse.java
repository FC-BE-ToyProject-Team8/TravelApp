package kr.co.fastcampus.travel.domain.itinerary.controller.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LodgeResponse(
    String placeName,
    String address,
    LocalDateTime checkInAt,
    LocalDateTime checkOutAt
) {

}
