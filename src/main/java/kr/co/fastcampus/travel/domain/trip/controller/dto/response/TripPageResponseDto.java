package kr.co.fastcampus.travel.domain.trip.controller.dto.response;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Builder
public record TripPageResponseDto(
    List<TripSummaryResponse> content,
    int page,
    int size,
    long totalCount,
    int start,
    int end,
    boolean prev,
    boolean next
) {

    public static TripPageResponseDto from(Page<TripSummaryResponse> dto) {
        Pageable pageable = dto.getPageable();
        int totalPage = dto.getTotalPages();

        int page = pageable.getPageNumber() + 1;
        int size = pageable.getPageSize();

        int tempEnd = (int) (Math.ceil(page / (double) size)) * size;

        int start = tempEnd - (size - 1);
        int end = Math.min(totalPage, tempEnd);
        boolean prev = start > 1;
        boolean next = totalPage > tempEnd;

        return TripPageResponseDto.builder()
            .content(dto.getContent())
            .page(page)
            .size(size)
            .totalCount(dto.getTotalElements())
            .start(start)
            .end(end)
            .prev(prev)
            .next(next)
            .build();
    }
}
