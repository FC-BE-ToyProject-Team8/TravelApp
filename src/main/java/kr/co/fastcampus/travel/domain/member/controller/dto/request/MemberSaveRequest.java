package kr.co.fastcampus.travel.domain.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        String email,
        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        String name,
        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        String nickname,
        @NotBlank(message = "패스워드는 필수 입력 사항입니다.")
        String password
) {

}
