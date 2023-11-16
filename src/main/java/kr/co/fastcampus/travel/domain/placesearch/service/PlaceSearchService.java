package kr.co.fastcampus.travel.domain.placesearch.service;

import kr.co.fastcampus.travel.common.exception.ApiRequestFailedException;
import kr.co.fastcampus.travel.common.exception.ApiUnauthorizedException;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.KakaoApiSearchResponse;
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

    private static String buildUrl(String query, Pageable pageable, String url) {
        UriComponents uriBuilder = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("query", query)
            .queryParam("page", pageable.getPageNumber())
            .queryParam("size", pageable.getPageSize())
            .build();
        return uriBuilder.toUriString();
    }

    public KakaoApiSearchResponse searchByKakaoApiKeyword(
        String query,
        Pageable pageable
    ) {
        HttpEntity<String> httpEntity = createKakaoHttpEntity();
        String url = buildUrl(query, pageable, KAKAO_SEARCH_API_URL);
        return getKakaoApiSearchResponse(url, httpEntity);
    }

    private KakaoApiSearchResponse getKakaoApiSearchResponse(
        String url,
        HttpEntity<String> httpEntity
    ) {
        try {
            ResponseEntity<KakaoApiSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                KakaoApiSearchResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ApiUnauthorizedException();
        } catch (ResourceAccessException e) {
            throw new ApiRequestFailedException();
        }
    }

    private HttpEntity<String> createKakaoHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoApiKey);
        return new HttpEntity<>(headers);
    }
}
