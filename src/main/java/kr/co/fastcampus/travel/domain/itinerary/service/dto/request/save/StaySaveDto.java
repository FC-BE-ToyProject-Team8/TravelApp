package kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;

public record StaySaveDto(
    String placeName,
    String address,
    LocalDateTime startAt,
    LocalDateTime endAt

) {

    public Stay toEntity() {
        return Stay.builder()
            .placeName(placeName())
            .address(address())
            .startAt(startAt())
            .endAt(endAt())
            .build();
    }
}
