package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record LodgeUpdateRequest(
        @NotBlank(message = "placeName을 입력해주세요.")
        @Size(max = 100, message = "placeName은 최대 100자입니다.")
        String placeName,
        @NotBlank(message = "address를 입력해주세요.")
        @Size(max = 200, message = "address는 최대 200자입니다.")
        String address,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt
) {

}
