package kr.co.fastcampus.travel.domain.secure.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginReqeust(
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    String email,
    @NotBlank(message = "패스워드는 필수 입력 사항입니다.")
    String password
) {

}
