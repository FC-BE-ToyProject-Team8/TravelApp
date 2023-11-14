package kr.co.fastcampus.travel.domain.placesearch.controller;

import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kr.co.fastcampus.travel.common.ApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PlaceSearchControllerTest extends ApiTest {

    @Test
    @DisplayName("카카오 API를 이용한 키워드 장소 검색, 페이지 X")
    void searchByKakaoApiKeyword() {
        // given
        String query = "부산";
        String url = String.format("/api/search-place?query=%s", query);

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<String> data = jsonPath.getList("data");
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(15);
        });
    }

    @Test
    @DisplayName("카카오 API를 이용한 키워드 장소 검색, 페이지 O")
    void searchByKakaoApiKeywordPage() {
        // given
        String query = "부산";
        int page = 2;
        int size = 10;
        String url = String.format("/api/search-place?query=%s&page=%d&size=%d", query, page, size);

        // when
        ExtractableResponse<Response> response = restAssuredGetWithToken(url);

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        List<String> data = jsonPath.getList("data");
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.size()).isEqualTo(10);
        });
    }
}
