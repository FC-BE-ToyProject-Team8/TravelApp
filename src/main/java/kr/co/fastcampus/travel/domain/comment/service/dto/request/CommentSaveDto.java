package kr.co.fastcampus.travel.domain.comment.service.dto.request;

import java.util.Objects;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.dto.request.MemberSaveDto;
import kr.co.fastcampus.travel.domain.member.service.dto.response.MemberInfoDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import lombok.Builder;

@Builder
public record CommentSaveDto(

    TripSaveDto trip,
    MemberSaveDto member,
    String content

) {

    public Comment toEntity() {
        return Comment.builder()
            .trip(getTrip())
            .member(getMember())
            .content(content)
            .build();
    }

    private Trip getTrip(){
        if (Objects.isNull(trip)) {
            return null;
        }

        return trip.toEntity();
    }

    private Member getMember(){
        if(Objects.isNull(member)){
            return null;
        }

        return member.toEntity();
    }
}
