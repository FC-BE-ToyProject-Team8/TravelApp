package kr.co.fastcampus.travel.domain.member.service.dto.request;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberSaveDto(
    String email,
    String name,
    String nickname,
    String password
) {

    public Member toEntity(){
        return Member.builder()
            .email(email)
            .nickname(nickname)
            .name(name)
            .password(password)
            .build();


    }
}
