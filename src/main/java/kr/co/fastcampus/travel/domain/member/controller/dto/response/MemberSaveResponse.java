package kr.co.fastcampus.travel.domain.member.controller.dto.response;

public record MemberSaveResponse(
        Long id,
        String email,
        String name,
        String nickname
) {

}
