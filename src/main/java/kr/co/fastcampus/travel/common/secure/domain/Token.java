package kr.co.fastcampus.travel.common.secure.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Token {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

    @Builder
    private Token(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
