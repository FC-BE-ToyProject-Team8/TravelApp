package kr.co.fastcampus.travel.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public final class RestAssuredUtils {

    private RestAssuredUtils() {
    }

    public static ExtractableResponse<Response> restAssuredPostBody(String url, Object request) {
        return restAssured()
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredGetWithToken(String url) {
        String accessToken = TokenUtils.getAccessToken();

        return restAssuredWithToken(accessToken)
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredGetWithToken(
        String url,
        String accessToken
    ) {
        return restAssuredWithToken(accessToken)
            .when()
            .get(url)
            .then().log().all()
            .extract();
    }


    public static ExtractableResponse<Response> restAssuredPostWithToken(
            String url,
            Object request
    ) {
        String accessToken = TokenUtils.getAccessToken();

        return restAssuredWithToken(accessToken)
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredPostWithToken(
        String url,
        Object request,
        String accessToken
    ) {
        return restAssuredWithToken(accessToken)
            .body(request)
            .when()
            .post(url)
            .then().log().all()
            .extract();
    }

    private static RequestSpecification restAssuredWithToken(String accessToken) {
        return restAssured()
                .header("Authorization", accessToken);
    }

    private static RequestSpecification restAssured() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
