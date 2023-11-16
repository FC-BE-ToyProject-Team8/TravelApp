package kr.co.fastcampus.travel.domain.comment.controller.dto.response;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.member.controller.dto.response.MemberResponse;
import lombok.Builder;

@Builder

public record CommentSummaryResponse(
    Long id,
    MemberResponse member,
    String content
) {

    public static CommentSummaryResponse from(Comment comment) {
        if (Objects.isNull(comment)) {
            return null;
        }

        return CommentSummaryResponse.builder()
            .id(comment.getId())
            .member(MemberResponse.from(comment.getMember()))
            .content(comment.getContent())
            .build();
    }
}
