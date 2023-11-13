package kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao;

import java.util.List;

public record KakaoApiSearchResponse(
    List<Place> documents,
    Meta meta
) {

}
