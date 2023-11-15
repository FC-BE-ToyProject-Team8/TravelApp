package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Builder
public record TripPageResponseDto(
    List<TripSummaryResponse> content,
    int pageNum,
    int pageSize,
    int totalPages,
    long totalElements,
    boolean first,
    boolean last
) {

    public static TripPageResponseDto from(Page<TripSummaryResponse> dto) {
        Pageable pageable = dto.getPageable();
        return TripPageResponseDto.builder()
            .content(dto.getContent())
            .pageNum(dto.getNumber() + 1)
            .pageSize(dto.getSize())
            .totalPages(dto.getTotalPages())
            .totalElements(dto.getTotalElements())
            .first(dto.isFirst())
            .last(dto.isLast())
            .build();
    }
}
