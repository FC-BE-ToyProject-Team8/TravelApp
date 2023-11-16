package kr.co.fastcampus.travel.domain.secure.controller.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
    String grantType,
    String accessToken,
    String refreshToken
) {

}
