package kr.co.fastcampus.travel.domain.itinerary.controller.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import lombok.Builder;

@Builder
public record StayResponse(
    String placeName,
    String address,
    LocalDateTime startAt,
    LocalDateTime endAt
) {

    public static StayResponse from(Stay stay) {
        return StayResponse.builder()
            .placeName(stay.getPlaceName())
            .address(stay.getAddress())
            .startAt(stay.getStartAt())
            .endAt(stay.getEndAt())
            .build();
    }
}
