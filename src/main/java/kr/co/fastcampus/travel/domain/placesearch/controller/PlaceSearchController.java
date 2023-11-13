package kr.co.fastcampus.travel.domain.placesearch.controller;

import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.PlaceSearchDtoMapper;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao.KakaoApiSearchResponse;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.response.PlaceInfoResponse;
import kr.co.fastcampus.travel.domain.placesearch.service.PlaceSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search-place")
@RequiredArgsConstructor
public class PlaceSearchController {

    private final PlaceSearchService placeSearchService;
    private final PlaceSearchDtoMapper mapper;

    @GetMapping("")
    public ResponseBody<List<PlaceInfoResponse>> searchByKakaoApiKeyword(
        @RequestParam String query,
        @PageableDefault(page = 1, size = 15) Pageable pageable
    ) {
        KakaoApiSearchResponse response =
            placeSearchService.searchByKakaoApiKeyword(query, pageable);
        return ResponseBody.ok(mapper.of(response.documents()));
    }
}
