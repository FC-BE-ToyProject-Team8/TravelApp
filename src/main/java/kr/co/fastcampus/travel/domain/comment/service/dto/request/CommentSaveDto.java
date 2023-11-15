package kr.co.fastcampus.travel.domain.comment.service.dto.request;

import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentSaveDto(
    String content
) {

    public Comment toEntity() {
        return Comment.builder()
            .content(content)
            .build();
    }
}
