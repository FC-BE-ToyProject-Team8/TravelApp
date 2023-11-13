package kr.co.fastcampus.travel.domain.itinerary.service.dto.response;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import lombok.Builder;

@Builder
public record ItineraryDto(
        Long id,
        RouteDto route,
        LodgeDto lodge,
        StayDto stay
) {

    public static ItineraryDto from(Itinerary itinerary) {
        return ItineraryDto.builder()
                .id(itinerary.getId())
                .route(getRoute(itinerary.getRoute()))
                .lodge(getLodge(itinerary.getLodge()))
                .stay(getStay(itinerary.getStay()))
                .build();
    }

    private static RouteDto getRoute(Route route) {
        if (Objects.isNull(route)) {
            return null;
        }

        return RouteDto.from(route);
    }

    private static LodgeDto getLodge(Lodge lodge) {
        if (Objects.isNull(lodge)) {
            return null;
        }

        return LodgeDto.from(lodge);
    }

    private static StayDto getStay(Stay stay) {
        if (Objects.isNull(stay)) {
            return null;
        }

        return StayDto.from(stay);
    }
}
