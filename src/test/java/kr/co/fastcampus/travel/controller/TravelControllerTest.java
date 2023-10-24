package kr.co.fastcampus.travel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TravelControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("여행 등록")
    void addTrip() {
        // given
        String url = "/api/trips";
        TripRequest request = new TripRequest(
            "이름", "2010-01-01", "2010-01-02", false
        );

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
            .when()
                .post(url)
            .then().log().all()
            .extract();

        // then
        JsonPath jsonPath = response.jsonPath();
        String status = jsonPath.getString("status");
        TripResponse data = jsonPath.getObject("data", TripResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertSoftly((softly) -> {
            softly.assertThat(status).isEqualTo("SUCCESS");
            softly.assertThat(data.id()).isNotNull();
            softly.assertThat(data.name()).isEqualTo("이름");
            softly.assertThat(data.startDate().toString()).isEqualTo("2010-01-01");
            softly.assertThat(data.endDate().toString()).isEqualTo("2010-01-02");
            softly.assertThat(data.isForeign()).isEqualTo(false);
        });
    }
}
