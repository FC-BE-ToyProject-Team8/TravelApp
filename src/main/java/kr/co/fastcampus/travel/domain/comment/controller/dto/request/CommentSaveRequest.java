package kr.co.fastcampus.travel.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentSaveRequest(
    @NotBlank(message = "빈 문자열은 입력할 수 없습니다.")
    String content
) {

}
