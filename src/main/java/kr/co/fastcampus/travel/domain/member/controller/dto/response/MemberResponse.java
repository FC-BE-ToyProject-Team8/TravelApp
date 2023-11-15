package kr.co.fastcampus.travel.domain.member.controller.dto.response;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberResponse(
    Long id,
    String name,
    String email,
    String nickname
) {

    public static MemberResponse from(Member member) {
        if (Objects.isNull(member)) {
            return null;
        }

        return MemberResponse.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .nickname(member.getNickname())
            .build();
    }
}
