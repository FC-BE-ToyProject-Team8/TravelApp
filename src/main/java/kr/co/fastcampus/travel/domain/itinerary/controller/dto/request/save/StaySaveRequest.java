package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

import java.time.LocalDateTime;

public record StaySaveRequest(
        String placeName,
        String address,
        LocalDateTime startAt,
        LocalDateTime endAt
) {

}
