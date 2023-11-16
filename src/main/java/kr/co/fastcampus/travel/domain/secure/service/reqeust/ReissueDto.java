package kr.co.fastcampus.travel.domain.secure.service.reqeust;

public record ReissueDto(
        String email,
        String refreshToken
) {

}
