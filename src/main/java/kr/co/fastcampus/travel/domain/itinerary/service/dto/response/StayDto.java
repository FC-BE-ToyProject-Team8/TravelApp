package kr.co.fastcampus.travel.domain.itinerary.service.dto.response;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import lombok.Builder;

@Builder
public record StayDto(
        String placeName,
        String address,
        LocalDateTime startAt,
        LocalDateTime endAt
) {

    public static StayDto from(Stay stay) {
        return StayDto.builder()
                .placeName(stay.getPlaceName())
                .address(stay.getAddress())
                .startAt(stay.getStartAt())
                .endAt(stay.getEndAt())
                .build();
    }
}
