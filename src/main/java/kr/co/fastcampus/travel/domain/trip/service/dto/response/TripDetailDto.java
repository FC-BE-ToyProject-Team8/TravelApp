package kr.co.fastcampus.travel.domain.trip.service.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.domain.comment.service.dto.response.CommentInfoDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder
public record TripDetailDto(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    boolean isForeign,
    int likeCount,
    List<ItineraryDto> itineraries,
    List<CommentInfoDto> comments
) {

    public static TripDetailDto from(Trip trip) {
        return TripDetailDto.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startDate(trip.getStartDate())
            .endDate(trip.getEndDate())
            .isForeign(trip.isForeign())
            .likeCount(trip.getLikeCount())
            .itineraries(getItineraries(trip))
            .comments(getComments(trip))
            .build();
    }

    private static List<ItineraryDto> getItineraries(Trip trip) {
        return trip.getItineraries().stream()
            .map(ItineraryDto::from)
            .collect(Collectors.toList());
    }

    private static List<CommentInfoDto> getComments(Trip trip) {
        return trip.getComments().stream()
            .map(CommentInfoDto::from)
            .collect(Collectors.toList());
    }
}
