package kr.co.fastcampus.travel.controller.util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.controller.response.LodgeResponse;
import kr.co.fastcampus.travel.controller.response.RouteResponse;
import kr.co.fastcampus.travel.controller.response.StayResponse;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import kr.co.fastcampus.travel.entity.Trip;

public class EntityToResponseConverter {

    private EntityToResponseConverter() {
    }

    public static TripResponse convertTripToResponse(Trip trip) {
        return TripResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startAt(trip.getStartDate())
            .endAt(trip.getEndDate())
            .itineraries(getItineraryResponses(trip))
            .build();
    }

    private static List<ItineraryResponse> getItineraryResponses(Trip trip) {
        return trip.getItineraries().stream()
            .map(EntityToResponseConverter::convertItineraryToResponse)
            .collect(Collectors.toList());
    }

    private static ItineraryResponse convertItineraryToResponse(Itinerary itinerary) {
        return ItineraryResponse.builder()
            .id(itinerary.getId())
            .route(Optional.ofNullable(itinerary.getRoute())
                .map(EntityToResponseConverter::convertRouteToResponse)
                .orElse(null))
            .lodge(Optional.ofNullable(itinerary.getLodge())
                .map(EntityToResponseConverter::convertLodgeToResponse)
                .orElse(null))
            .stay(Optional.ofNullable(itinerary.getStay())
                .map(EntityToResponseConverter::convertStayToResponse)
                .orElse(null))
            .build();
    }

    private static RouteResponse convertRouteToResponse(Route route) {
        return RouteResponse.builder()
            .transportation(route.getTransportation())
            .departurePlaceName(route.getDeparturePlaceName())
            .departureAddress(route.getDepartureAddress())
            .destinationPlaceName(route.getDestinationPlaceName())
            .destinationAddress(route.getDestinationAddress())
            .departureAt(route.getDepartureAt())
            .arriveAt(route.getArriveAt())
            .build();
    }

    private static LodgeResponse convertLodgeToResponse(Lodge lodge) {
        return LodgeResponse.builder()
            .placeName(lodge.getPlaceName())
            .address(lodge.getAddress())
            .checkInAt(lodge.getCheckInAt())
            .checkOutAt(lodge.getCheckOutAt())
            .build();
    }

    private static StayResponse convertStayToResponse(Stay stay) {
        return StayResponse.builder()
            .placeName(stay.getPlaceName())
            .address(stay.getAddress())
            .startAt(stay.getStartAt())
            .endAt(stay.getEndAt())
            .build();
    }
}
