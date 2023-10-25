package kr.co.fastcampus.travel.controller.request;

import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import lombok.Builder;

@Builder
public record ItineraryRequest(
    Route route,
    Lodge lodge,
    Stay stay

) {

}
