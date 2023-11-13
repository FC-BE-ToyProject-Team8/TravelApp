package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

import java.time.LocalDateTime;

public record LodgeUpdateRequest(
        String placeName,
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
) {

}
