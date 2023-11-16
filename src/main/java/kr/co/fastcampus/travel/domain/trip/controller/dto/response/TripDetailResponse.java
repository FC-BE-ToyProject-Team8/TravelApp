package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.time.LocalDate;
import java.util.List;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentSummaryResponse;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import lombok.Builder;

@Builder
public record TripDetailResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean isForeign,
        long likeCount,
        List<ItineraryResponse> itineraries,
        List<CommentSummaryResponse> comments
) {

}
