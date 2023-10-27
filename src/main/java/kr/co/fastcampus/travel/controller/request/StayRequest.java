package kr.co.fastcampus.travel.controller.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record StayRequest(
    String placeName,
    String address,
    LocalDateTime startAt,
    LocalDateTime endAt

) {

}
