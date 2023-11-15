package kr.co.fastcampus.travel.domain.trip.service.dto.request;

import java.time.LocalDate;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripSaveDto(
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign
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
