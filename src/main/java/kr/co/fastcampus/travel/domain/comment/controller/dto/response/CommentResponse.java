package kr.co.fastcampus.travel.domain.comment.controller.dto.response;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.member.controller.dto.response.MemberResponse;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import lombok.Builder;

@Builder

public record CommentResponse(
    Long id,
    MemberResponse member,
    TripSummaryResponse trip,
    String content
) {

    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .member(MemberResponse.from(comment.getMember()))
            .trip(TripSummaryResponse.from(comment.getTrip()))
            .content(comment.getContent())
            .build();
    }
}
