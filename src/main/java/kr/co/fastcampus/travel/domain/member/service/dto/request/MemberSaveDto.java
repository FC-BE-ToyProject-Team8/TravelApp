package kr.co.fastcampus.travel.domain.member.service.dto.request;

import kr.co.fastcampus.travel.domain.member.entity.Member;

public record MemberSaveDto(
        String email,
        String name,
        String nickname,
        String password
) {

    public Member toEntity(String encodePassword) {
        return Member.builder()
                .email(email)
                .name(name)
                .nickname(nickname)
                .password(encodePassword)
                .build();
    }
}
