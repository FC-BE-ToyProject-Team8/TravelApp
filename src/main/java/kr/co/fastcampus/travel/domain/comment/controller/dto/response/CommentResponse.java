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
            .member(getMember(comment.getMember()))
            .trip(getTrip(comment.getTrip()))
            .content(comment.getContent())
            .build();
    }

    private static MemberResponse getMember(Member member) {
        if (Objects.isNull(member)) {
            return null;
        }

        return MemberResponse.from(member);
    }

    private static TripSummaryResponse getTrip(Trip trip) {
        if (Objects.isNull(trip)) {
            return null;
        }

        return TripSummaryResponse.from(trip);
    }
}
