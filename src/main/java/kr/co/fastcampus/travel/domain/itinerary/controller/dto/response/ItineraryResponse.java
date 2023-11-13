package kr.co.fastcampus.travel.domain.itinerary.controller.dto.response;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import lombok.Builder;

@Builder
public record ItineraryResponse(
        Long id,
        RouteResponse route,
        LodgeResponse lodge,
        StayResponse stay
) {

    public static ItineraryResponse from(Itinerary itinerary) {
        return ItineraryResponse.builder()
                .id(itinerary.getId())
                .route(getRoute(itinerary.getRoute()))
                .lodge(getLodge(itinerary.getLodge()))
                .stay(getStay(itinerary.getStay()))
                .build();
    }

    private static RouteResponse getRoute(Route route) {
        if (Objects.isNull(route)) {
            return null;
        }

        return RouteResponse.from(route);
    }

    private static LodgeResponse getLodge(Lodge lodge) {
        if (Objects.isNull(lodge)) {
            return null;
        }

        return LodgeResponse.from(lodge);
    }

    private static StayResponse getStay(Stay stay) {
        if (Objects.isNull(stay)) {
            return null;
        }

        return StayResponse.from(stay);
    }

}
