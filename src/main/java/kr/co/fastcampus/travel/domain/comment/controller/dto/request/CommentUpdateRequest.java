package kr.co.fastcampus.travel.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentUpdateRequest(
    @NotBlank
    String content
) {

}
