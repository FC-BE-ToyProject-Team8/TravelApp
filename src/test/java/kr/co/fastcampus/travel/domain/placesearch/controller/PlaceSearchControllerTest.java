package kr.co.fastcampus.travel.domain.placesearch.controller;


import static kr.co.fastcampus.travel.common.PlaceSearchTestUtils.API_PLACE_SEARCH;
import static kr.co.fastcampus.travel.common.PlaceSearchTestUtils.createKakaoResponse;
import static kr.co.fastcampus.travel.common.PlaceSearchTestUtils.createPlaceInfoResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.PlaceSearchDtoMapper;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.response.PlaceInfoResponse;
import kr.co.fastcampus.travel.domain.placesearch.service.PlaceSearchService;
import kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao.KakaoApiSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class PlaceSearchControllerTest {

    @Mock
    private PlaceSearchService placeSearchService;
    @InjectMocks
    private PlaceSearchController placeSearchController;

    @Mock
    private PlaceSearchDtoMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(placeSearchController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    @DisplayName("Api 키워드 장소 검색 페이지 O")
    public void searchByMockApiPage() throws Exception {
        // Given
        String query = "결과";
        Pageable pageable = PageRequest.of(1, 15);
        KakaoApiSearchResponse kakaoResponse = createKakaoResponse();
        List<PlaceInfoResponse> response = createPlaceInfoResponse();
        when(placeSearchService.searchByKakaoApiKeyword(anyString(), any()))
            .thenReturn(kakaoResponse);
        when(mapper.of(kakaoResponse.documents())).thenReturn(response);

        // When & Then
        mockMvc.perform(get(API_PLACE_SEARCH)
                .param("query", query)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize()))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data[0].name").value(response.get(0).name()))
            .andExpect(jsonPath("$.data[0].address").value(response.get(0).address()))
            .andExpect(jsonPath("$.data[0].placeUrl").value(response.get(0).placeUrl()));
    }

    @Test
    @DisplayName("Api 키워드 장소 검색 페이지 X")
    public void searchByMockApi() throws Exception {
        // Given
        String query = "결과";
        KakaoApiSearchResponse kakaoResponse = createKakaoResponse();
        List<PlaceInfoResponse> response = createPlaceInfoResponse();
        when(placeSearchService.searchByKakaoApiKeyword(anyString(), any()))
            .thenReturn(kakaoResponse);
        when(mapper.of(kakaoResponse.documents())).thenReturn(response);

        // When & Then
        mockMvc.perform(get(API_PLACE_SEARCH)
                .param("query", query)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("SUCCESS"))
            .andExpect(jsonPath("$.data[0].name").value(response.get(0).name()))
            .andExpect(jsonPath("$.data[0].address").value(response.get(0).address()))
            .andExpect(jsonPath("$.data[0].placeUrl").value(response.get(0).placeUrl()));
    }
}
