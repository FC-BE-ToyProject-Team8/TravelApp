package kr.co.fastcampus.travel.domain.secure.service.response;

import kr.co.fastcampus.travel.common.secure.domain.Token;
import lombok.Builder;

@Builder
public record TokenDto(
    String grantType,
    String accessToken,
    String refreshToken
) {

    public static TokenDto from(Token token) {
        return TokenDto.builder()
            .grantType(token.getGrantType())
            .refreshToken(token.getRefreshToken())
            .accessToken(token.getAccessToken())
            .build();
    }
}
