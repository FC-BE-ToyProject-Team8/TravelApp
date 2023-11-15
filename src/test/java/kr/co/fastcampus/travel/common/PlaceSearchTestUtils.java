package kr.co.fastcampus.travel.common;

import java.util.List;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.response.PlaceInfoResponse;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.KakaoApiSearchResponse;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.Meta;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.Place;

public class PlaceSearchTestUtils {

    public static final String API_PLACE_SEARCH = "/api/search-place";

    private PlaceSearchTestUtils() {
    }

    public static KakaoApiSearchResponse createKakaoResponse() {
        Place place = Place.builder()
            .addressName("서울 양천구 신정4동 888-41")
            .placeName("결과가나와")
            .placeUrl("http://place.map.kakao.com/16934974")
            .roadAddressName("서울 양천구 신정중앙로 101-4")
            .build();
        List<Place> places = List.of(place);
        Meta meta = new Meta(true, 1, 1, null);
        return new KakaoApiSearchResponse(places, meta);
    }

    public static List<PlaceInfoResponse> createPlaceInfoResponse() {
        return List.of(PlaceInfoResponse.builder()
            .name("결과가나와")
            .category("")
            .placeUrl("http://place.map.kakao.com/16934974")
            .address("서울 양천구 신정중앙로 101-4")
            .build());
    }
}
