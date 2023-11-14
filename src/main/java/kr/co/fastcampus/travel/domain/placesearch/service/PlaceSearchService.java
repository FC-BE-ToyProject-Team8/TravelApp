package kr.co.fastcampus.travel.domain.placesearch.service;

import java.security.Principal;
import kr.co.fastcampus.travel.common.exception.ApiRequestFailedException;
import kr.co.fastcampus.travel.common.exception.ApiUnauthorizedException;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.entity.RoleType;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao.KakaoApiSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    private final MemberService memberService;
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
        Pageable pageable,
        Principal principal
    ) {
        Member member = memberService.findMemberByEmail(principal.getName());
        if (member.getRole() != RoleType.User) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        String url = buildUrl(query, pageable, KAKAO_SEARCH_API_URL);
        ResponseEntity<KakaoApiSearchResponse>
            response = getKakaoApiSearchResponse(httpEntity, url);
        return response.getBody();
    }

    private ResponseEntity<KakaoApiSearchResponse> getKakaoApiSearchResponse(
        HttpEntity<String> httpEntity,
        String url
    ) {
        try {
            return restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                KakaoApiSearchResponse.class
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            // API KEY가 다를 때
            throw new ApiUnauthorizedException();
        } catch (ResourceAccessException e) {
            // 네트워크 등 문제로 API 요청 실패
            throw new ApiRequestFailedException();
        }
    }
}
