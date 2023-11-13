package kr.co.fastcampus.travel.domain.itinerary.controller.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import lombok.Builder;

@Builder
public record LodgeResponse(
        String placeName,
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
) {

    public static LodgeResponse from(Lodge lodge) {
        return LodgeResponse.builder()
                .placeName(lodge.getPlaceName())
                .address(lodge.getAddress())
                .checkInAt(lodge.getCheckInAt())
                .checkOutAt(lodge.getCheckOutAt())
                .build();
    }
}
