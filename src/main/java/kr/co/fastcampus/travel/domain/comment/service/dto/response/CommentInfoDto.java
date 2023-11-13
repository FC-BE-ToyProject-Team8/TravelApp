package kr.co.fastcampus.travel.domain.comment.service.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentResponse;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.dto.response.MemberInfoDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import lombok.Builder;

@Builder
public record CommentInfoDto(
    Long id,
    TripInfoDto trip,
    MemberInfoDto member,
    String content
) {

    public static CommentInfoDto from(Comment comment){
        return CommentInfoDto.builder()
            .id(comment.getId())
            .member(MemberInfoDto.from(comment.getMember()))
            .trip(TripInfoDto.from(comment.getTrip()))
            .content(comment.getContent())
            .build();
    }
}
