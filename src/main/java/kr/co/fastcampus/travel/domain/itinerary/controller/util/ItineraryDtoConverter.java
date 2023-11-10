package kr.co.fastcampus.travel.domain.itinerary.controller.util;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.RouteRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.StayRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.LodgeResponse;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.RouteResponse;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.StayResponse;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Lodge;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Stay;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;

public class ItineraryDtoConverter {

    private ItineraryDtoConverter() {
    }

    public static Itinerary toItinerary(ItineraryRequest request) {
        return Itinerary.builder()
            .route(toRoute(request.route()))
            .lodge(toLodge(request.lodge()))
            .stay(toStay(request.stay()))
            .build();
    }

    public static List<ItineraryResponse> getItineraryResponses(Trip trip) {
        return trip.getItineraries().stream()
            .map(ItineraryDtoConverter::toItineraryResponse)
            .collect(Collectors.toList());
    }

    public static ItineraryResponse toItineraryResponse(Itinerary itinerary) {
        return ItineraryResponse.builder()
            .id(itinerary.getId())
            .route(toRouteResponse(itinerary.getRoute()))
            .lodge(toLodgeResponse(itinerary.getLodge()))
            .stay(toStayResponse(itinerary.getStay()))
            .build();
    }

    private static Route toRoute(RouteRequest request) {
        if (isNull(request)) {
            return null;
        }

        return Route.builder()
            .transportation(request.transportation())
            .departurePlaceName(request.departurePlaceName())
            .departureAddress(request.departureAddress())
            .destinationPlaceName(request.destinationPlaceName())
            .destinationAddress(request.destinationAddress())
            .departureAt(request.departureAt())
            .arriveAt(request.arriveAt())
            .build();
    }

    private static Lodge toLodge(LodgeRequest request) {
        if (isNull(request)) {
            return null;
        }

        return Lodge.builder()
            .placeName(request.placeName())
            .address(request.address())
            .checkInAt(request.checkInAt())
            .checkOutAt(request.checkOutAt())
            .build();
    }

    private static Stay toStay(StayRequest request) {
        if (isNull(request)) {
            return null;
        }

        return Stay.builder()
            .placeName(request.placeName())
            .address(request.address())
            .startAt(request.startAt())
            .endAt(request.endAt())
            .build();
    }

    private static RouteResponse toRouteResponse(Route route) {
        if (isNull(route)) {
            return null;
        }

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

    private static LodgeResponse toLodgeResponse(Lodge lodge) {
        if (isNull(lodge)) {
            return null;
        }

        return LodgeResponse.builder()
            .placeName(lodge.getPlaceName())
            .address(lodge.getAddress())
            .checkInAt(lodge.getCheckInAt())
            .checkOutAt(lodge.getCheckOutAt())
            .build();
    }

    private static StayResponse toStayResponse(Stay stay) {
        if (isNull(stay)) {
            return null;
        }

        return StayResponse.builder()
            .placeName(stay.getPlaceName())
            .address(stay.getAddress())
            .startAt(stay.getStartAt())
            .endAt(stay.getEndAt())
            .build();
    }
}
