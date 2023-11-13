package kr.co.fastcampus.travel.domain.member.controller.response;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberResponse(
    Long id,
    String email,
    String name,
    String nickname
) {

    public static MemberResponse from(Member member){
        return MemberResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .nickname(member.getNickname())
            .build();
    }

}
