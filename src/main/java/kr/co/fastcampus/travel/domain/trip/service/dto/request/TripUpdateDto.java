package kr.co.fastcampus.travel.domain.trip.service.dto.request;

import java.time.LocalDate;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;

public record TripUpdateDto(
        String name,
        LocalDate startDate,
        LocalDate endDate,
        Boolean isForeign
) {

    public Trip toEntity() {
        return Trip.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .isForeign(isForeign)
                .build();
    }
}
