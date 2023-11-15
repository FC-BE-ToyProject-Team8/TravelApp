package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record RouteUpdateRequest(
        @NotBlank(message = "transporation을 입력해주세요.")
        @Size(max = 100, message = "transportation은 최대 100자입니다.")
        String transportation,
        @NotBlank(message = "departurePlaceName을 입력해주세요.")
        @Size(max = 100, message = "transporation은 최대 100자입니다.")
        String departurePlaceName,
        @NotBlank(message = "departureAddress를 입력해주세요.")
        @Size(max = 200, message = "departureAddress는 최대 200자입니다.")
        String departureAddress,
        @NotBlank(message = "destinationPlaceName을 입력해주세요.")
        @Size(max = 100, message = "destinationPlaceName은 최대 100자입니다.")
        String destinationPlaceName,
        @NotBlank(message = "destinationAddress를 입력해주세요.")
        @Size(max = 200, message = "destinationAddress는 최대 200자입니다.")
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

}
