package kr.co.fastcampus.travel.domain.trip.service.dto.response;

import java.time.LocalDate;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripInfoDto(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    Long likeCount
) {

    public static TripInfoDto from(Trip trip) {
        System.out.println("dto 담기 전 여행객체 " +trip.getLikeCount());
        return TripInfoDto.builder()
                .id(trip.getId())
                .name(trip.getName())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .isForeign(trip.isForeign())
                .likeCount(trip.getLikeCount())
                .build();
    }
}
