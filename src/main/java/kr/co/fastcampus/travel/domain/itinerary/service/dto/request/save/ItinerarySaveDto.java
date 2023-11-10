package kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import lombok.Builder;

@Builder
public record ItinerarySaveDto(
        RouteSaveDto route,
        LodgeSaveDto lodge,
        StaySaveDto stay
) {

    public Itinerary toEntity() {
        return Itinerary.builder()
                .route(getRoute())
                .lodge(getLodge())
                .stay(getStay())
                .build();
    }

    private Route getRoute() {
        if (Objects.isNull(route)) {
            return null;
        }

        return route.toEntity();
    }

    private Lodge getLodge() {
        if (Objects.isNull(lodge)) {
            return null;
        }

        return lodge.toEntity();
    }

    private Stay getStay() {
        if (Objects.isNull(stay)) {
            return null;
        }

        return stay.toEntity();
    }
}
