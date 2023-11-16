package kr.co.fastcampus.travel.domain.itinerary.service.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import lombok.Builder;

@Builder
public record LodgeDto(
    String placeName,
    String address,
    LocalDateTime checkInAt,
    LocalDateTime checkOutAt
) {

    public static LodgeDto from(Lodge lodge) {
        return LodgeDto.builder()
            .placeName(lodge.getPlaceName())
            .address(lodge.getAddress())
            .checkInAt(lodge.getCheckInAt())
            .checkOutAt(lodge.getCheckOutAt())
            .build();
    }
}
