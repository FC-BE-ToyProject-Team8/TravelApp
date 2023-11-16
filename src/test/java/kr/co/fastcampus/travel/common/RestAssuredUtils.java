package kr.co.fastcampus.travel.common;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createLoginRequest;

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

    public static ExtractableResponse<Response> restAssuredGet(
            String url
    ) {
        return restAssured()
                .when()
                .get(url)
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

    public static ExtractableResponse<Response> restAssuredPostWithToken(
            String url
    ) {
        String accessToken = TokenUtils.getAccessToken();

        return restAssuredWithToken(accessToken)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredDeleteWithToken(
            String url
    ) {
        String accessToken = TokenUtils.getAccessToken();

        return restAssuredWithToken(accessToken)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredDeleteWithTokenLogin(
            String url
    ) {
        String accessToken = TokenUtils.getAccessToken(createLoginRequest());

        return restAssuredWithToken(accessToken)
                .when()
                .delete(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredDeleteWithToken(
        String url,
        String accessToken
    ) {
        return restAssuredWithToken(accessToken)
            .when()
            .delete(url)
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

    public static ExtractableResponse<Response> restAssuredPutWithToken(
            String url,
            Object request
    ) {
        String accessToken = TokenUtils.getAccessToken();

        return restAssuredWithToken(accessToken)
                .body(request)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredPutWithToken(
            String url,
            Object request,
            String accessToken
    ) {
        return restAssuredWithToken(accessToken)
                .body(request)
                .when()
                .put(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredPostWithTokenLogin(
            String url
    ) {
        String accessToken = TokenUtils.getAccessToken(createLoginRequest());

        return restAssuredWithToken(accessToken)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> restAssuredPostWithTokenLogin(
            String url,
            Object request
    ) {
        String accessToken = TokenUtils.getAccessToken(createLoginRequest());

        return restAssuredWithToken(accessToken)
                .body(request)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }
}
