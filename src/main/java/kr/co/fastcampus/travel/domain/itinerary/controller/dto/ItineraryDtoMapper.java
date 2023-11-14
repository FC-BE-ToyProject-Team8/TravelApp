package kr.co.fastcampus.travel.domain.itinerary.controller.dto;

import java.util.ArrayList;
import java.util.List;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.LodgeSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.RouteSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.StaySaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.ItineraryUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.LodgeUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.RouteUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.StayUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.LodgeSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.RouteSaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.StaySaveDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.ItineraryUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.LodgeUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.RouteUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.StayUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ItineraryDtoMapper {

    // Request
    RouteSaveDto of(RouteSaveRequest request);

    LodgeSaveDto of(LodgeSaveRequest request);

    StaySaveDto of(StaySaveRequest request);

    ItinerarySaveDto of(ItinerarySaveRequest request);

    default List<ItinerarySaveDto> of(ItinerariesSaveRequest request) {
        if (request == null) {
            return null;
        }

        List<ItinerarySaveDto> list = new ArrayList<>(request.itineraries().size());
        for (ItinerarySaveRequest item : request.itineraries()) {
            list.add(of(item));
        }

        return list;
    }

    RouteUpdateDto of(RouteUpdateRequest request);

    LodgeUpdateDto of(LodgeUpdateRequest request);

    StayUpdateDto of(StayUpdateRequest request);

    ItineraryUpdateDto of(ItineraryUpdateRequest request);

    // Response
    RouteSaveRequest of(RouteSaveDto dto);

    LodgeSaveRequest of(LodgeSaveDto dto);

    StaySaveRequest of(StaySaveDto dto);

    ItineraryResponse of(ItineraryDto dto);

    default List<ItineraryResponse> of(List<ItineraryDto> itineraryDto) {
        if (itineraryDto == null) {
            return null;
        }

        List<ItineraryResponse> list = new ArrayList<>(itineraryDto.size());
        for (ItineraryDto item : itineraryDto) {
            list.add(of(item));
        }

        return list;
    }

    TripResponse of(TripItineraryInfoDto dto);
}
