package kr.co.fastcampus.travel.domain.placesearch.service;

import static kr.co.fastcampus.travel.common.PlaceSearchTestUtils.createKakaoResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import kr.co.fastcampus.travel.common.exception.ApiRequestFailedException;
import kr.co.fastcampus.travel.common.exception.ApiUnauthorizedException;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.KakaoApiSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class PlaceSearchServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PlaceSearchService placeSearchService;

    @Test
    @DisplayName("API 키워드 장소 검색")
    public void searchByKakaoApiKeywordPage() {
        // Given
        String query = "결과";
        Pageable pageable = PageRequest.of(1, 10);
        KakaoApiSearchResponse expectedResponse = createKakaoResponse();

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(KakaoApiSearchResponse.class)
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        // When
        KakaoApiSearchResponse result = placeSearchService.searchByKakaoApiKeyword(query, pageable);

        // then
        assertThat(result.meta().totalCount()).isEqualTo(1);
        assertThat(result.documents().get(0).roadAddressName()).isEqualTo("서울 양천구 신정중앙로 101-4");
        assertThat(result.documents().get(0).placeUrl()).isEqualTo(
            "http://place.map.kakao.com/16934974");
    }

    @Test
    @DisplayName("API 키워드 장소 검색 인가 실패")
    public void searchByKakaoApiUnauthorized() {
        // Given
        String query = "결과";
        Pageable pageable = PageRequest.of(1, 10);

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(KakaoApiSearchResponse.class)
        )).thenThrow(new ApiUnauthorizedException());

        assertThatThrownBy(() -> placeSearchService.searchByKakaoApiKeyword(query, pageable))
            .isInstanceOf(ApiUnauthorizedException.class);
    }
    @Test
    @DisplayName("API 키워드 장소 검색 인가 실패")
    public void searchByKakaoApiRequestFail() {
        // Given
        String query = "결과";
        Pageable pageable = PageRequest.of(1, 10);

        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(KakaoApiSearchResponse.class)
        )).thenThrow(new ApiRequestFailedException());

        assertThatThrownBy(() -> placeSearchService.searchByKakaoApiKeyword(query, pageable))
            .isInstanceOf(ApiRequestFailedException.class);
    }
}
