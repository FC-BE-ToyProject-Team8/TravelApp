package kr.co.fastcampus.travel.domain.comment.controller;

import jakarta.validation.Valid;
import java.security.Principal;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.comment.controller.dto.CommentDtoMapper;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentSaveRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentUpdateRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentResponse;
import kr.co.fastcampus.travel.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentDtoMapper mapper;

    @PostMapping
    public ResponseBody<CommentResponse> saveComment(
        @RequestParam Long tripId,
        @Valid @RequestBody CommentSaveRequest request,
        Principal principal
    ) {
        String memberEmail = principal.getName();
        var response =
            commentService.addComment(tripId, memberEmail,
                mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }

    @PutMapping("/{commentId}")
    public ResponseBody<CommentResponse> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentUpdateRequest request,
        Principal principal
    ) {
        String memberEmail = principal.getName();
        var response =
            commentService.editComment(commentId, memberEmail, mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }

}
