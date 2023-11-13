package kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;

public record LodgeSaveDto(
        String placeName,
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
) {

    public Lodge toEntity() {
        return Lodge.builder()
                .placeName(placeName())
                .address(address())
                .checkInAt(checkInAt())
                .checkOutAt(checkOutAt())
                .build();
    }
}
