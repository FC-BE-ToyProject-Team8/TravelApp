package kr.co.fastcampus.travel.controller.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StayResponse(
        String placeName,
        String address,
        LocalDateTime startAt,
        LocalDateTime endAt
) {

}
