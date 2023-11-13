package kr.co.fastcampus.travel.domain.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberSaveRequest(
        @NotBlank
        String email,
        @NotBlank
        String name,
        @NotBlank
        String nickname,
        @NotBlank
        String password
) {

}
