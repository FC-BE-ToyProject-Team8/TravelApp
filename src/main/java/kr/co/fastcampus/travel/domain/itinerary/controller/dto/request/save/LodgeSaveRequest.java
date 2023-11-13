package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

import java.time.LocalDateTime;

public record LodgeSaveRequest(
        String placeName,
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
) {

}
