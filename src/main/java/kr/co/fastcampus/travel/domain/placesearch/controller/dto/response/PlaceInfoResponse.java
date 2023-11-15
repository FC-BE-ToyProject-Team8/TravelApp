package kr.co.fastcampus.travel.domain.placesearch.controller.dto.response;

import lombok.Builder;

@Builder
public record PlaceInfoResponse(
    String name,
    String category,
    String address,
    String placeUrl
) {

}
