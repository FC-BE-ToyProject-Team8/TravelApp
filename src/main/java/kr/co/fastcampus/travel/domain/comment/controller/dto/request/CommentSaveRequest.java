package kr.co.fastcampus.travel.domain.comment.controller.dto.request;

import lombok.Builder;

@Builder
public record CommentSaveRequest(
    String content
) {

}
