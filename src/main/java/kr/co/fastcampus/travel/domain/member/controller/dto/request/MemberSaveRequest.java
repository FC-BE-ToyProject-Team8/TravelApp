package kr.co.fastcampus.travel.domain.member.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        @Email(message = "이메일 형식으로 입력해 주세요.")
        String email,

        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        String name,

        @NotBlank(message = "닉네임은 필수 입력 사항입니다.")
        String nickname,

        @NotBlank(message = "패스워드는 필수 입력 사항입니다.")
        @Min(value = 8, message = "8자리 이상의 비밀번호를 입력해 주세요")
        String password
) {

}
