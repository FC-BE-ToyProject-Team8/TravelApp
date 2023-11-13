package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

import java.time.LocalDateTime;

public record StayUpdateRequest(
        String placeName,
        String address,
        LocalDateTime startAt,
        LocalDateTime endAt
) {

}
