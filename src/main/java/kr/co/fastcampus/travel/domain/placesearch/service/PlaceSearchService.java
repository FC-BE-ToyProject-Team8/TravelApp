package kr.co.fastcampus.travel.domain.placesearch.service;

import kr.co.fastcampus.travel.common.exception.ApiRequestFailedException;
import kr.co.fastcampus.travel.common.exception.ApiUnauthorizedException;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao.KakaoApiSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceSearchService {

    private static final String KAKAO_SEARCH_API_URL =
        "https://dapi.kakao.com/v2/local/search/keyword.json";

    private final RestTemplate restTemplate;
    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public KakaoApiSearchResponse searchByKakaoApiKeyword(String query, Pageable pageable) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + kakaoApiKey);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            UriComponents uriBuilder = UriComponentsBuilder
                .fromUriString(KAKAO_SEARCH_API_URL)
                .queryParam("query", query)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize())
                .build();

            ResponseEntity<KakaoApiSearchResponse> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                httpEntity,
                KakaoApiSearchResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            // API KEY가 다를 때
            throw new ApiUnauthorizedException();
        } catch (ResourceAccessException e) {
            // 네트워크 등 문제로 API 요청 실패
            throw new ApiRequestFailedException();
        }
    }
}
