package kr.co.fastcampus.travel.domain.secure.controller.dto.request;

public record ReissueRequest(
        String email,
        String refreshToken
) {

}
