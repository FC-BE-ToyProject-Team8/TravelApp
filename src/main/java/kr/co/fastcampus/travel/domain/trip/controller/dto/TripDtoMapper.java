package kr.co.fastcampus.travel.domain.trip.controller.dto;

import java.util.List;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripUpdateRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TripDtoMapper {

    // Request
    TripSaveDto of(TripSaveRequest request);

    TripUpdateDto of(TripUpdateRequest request);

    // Response
    TripResponse of(TripItineraryInfoDto dto);

    TripSummaryResponse of(TripInfoDto dto);

    List<TripSummaryResponse> of(List<TripInfoDto> dto);

    default List<TripSummaryResponse> of(Page<TripInfoDto> page) {
        return page.getContent().stream()
            .map(this::of)
            .toList();
    }
}
