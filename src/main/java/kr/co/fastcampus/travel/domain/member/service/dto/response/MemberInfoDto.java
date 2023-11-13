package kr.co.fastcampus.travel.domain.member.service.dto.response;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoDto(
    Long id,
    String email,
    String name,
    String nickname
) {

    public static MemberInfoDto from(Member member) {
        return MemberInfoDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .nickname(member.getNickname())
            .build();
    }
}
