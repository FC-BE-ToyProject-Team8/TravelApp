package kr.co.fastcampus.travel.controller.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.controller.request.RouteRequest;
import kr.co.fastcampus.travel.controller.request.StayRequest;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.*;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
import kr.co.fastcampus.travel.entity.Trip;

public class TravelDtoConverter {

    private TravelDtoConverter() {
    }

    public static TripResponse toTripResponse(Trip trip) {
        return TripResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startAt(trip.getStartDate())
            .endAt(trip.getEndDate())
            .isForeign(trip.isForeign())
            .itineraries(getItineraryResponses(trip))
            .build();
    }

    public static TripSummaryResponse toTripSummaryResponse(Trip trip) {
        return TripSummaryResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startAt(trip.getStartDate())
            .endAt(trip.getEndDate())
            .isForeign(trip.isForeign())
            .build();
    }

    public static List<TripSummaryResponse> toTripSummaryResponses(List<Trip> trips) {
        return trips.stream()
            .map(TravelDtoConverter::toTripSummaryResponse)
            .toList();
    }

    public static Trip toTrip(TripRequest request) {
        return Trip.builder()
            .name(request.name())
            .startDate(LocalDate.parse(request.startDate()))
            .endDate(LocalDate.parse(request.endDate()))
            .isForeign(request.isForeign())
            .build();
    }

    public static Itinerary toItinerary(ItineraryRequest request) {
        return Itinerary.builder()
            .route(request.route() == null ? null : toRoute(request.route()))
            .lodge(request.lodge() == null ? null : toLodge(request.lodge()))
            .stay(request.stay() == null ? null : toStay(request.stay()))
            .build();
    }

    private static List<ItineraryResponse> getItineraryResponses(Trip trip) {
        return trip.getItineraries().stream()
            .map(TravelDtoConverter::toItineraryResponse)
            .collect(Collectors.toList());
    }

    private static ItineraryResponse toItineraryResponse(Itinerary itinerary) {
        return ItineraryResponse.builder()
            .id(itinerary.getId())
            .route(Optional.ofNullable(itinerary.getRoute())
                .map(TravelDtoConverter::toRouteResponse)
                .orElse(null))
            .lodge(Optional.ofNullable(itinerary.getLodge())
                .map(TravelDtoConverter::toLodgeResponse)
                .orElse(null))
            .stay(Optional.ofNullable(itinerary.getStay())
                .map(TravelDtoConverter::toStayResponse)
                .orElse(null))
            .build();
    }

    private static RouteResponse toRouteResponse(Route route) {
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
        return LodgeResponse.builder()
            .placeName(lodge.getPlaceName())
            .address(lodge.getAddress())
            .checkInAt(lodge.getCheckInAt())
            .checkOutAt(lodge.getCheckOutAt())
            .build();
    }

    private static StayResponse toStayResponse(Stay stay) {
        return StayResponse.builder()
            .placeName(stay.getPlaceName())
            .address(stay.getAddress())
            .startAt(stay.getStartAt())
            .endAt(stay.getEndAt())
            .build();
    }
    private static Route toRoute(RouteRequest routeRequest) {
        return Route.builder()
            .transportation(routeRequest.transportation())
            .departurePlaceName(routeRequest.departurePlaceName())
            .departureAddress(routeRequest.departureAddress())
            .destinationPlaceName(routeRequest.destinationPlaceName())
            .destinationAddress(routeRequest.destinationAddress())
            .departureAt(routeRequest.departureAt())
            .arriveAt(routeRequest.arriveAt())
            .build();
    }

    private static Lodge toLodge(LodgeRequest lodgeRequest) {
        return Lodge.builder()
            .placeName(lodgeRequest.placeName())
            .address(lodgeRequest.address())
            .checkInAt(lodgeRequest.checkInAt())
            .checkOutAt(lodgeRequest.checkOutAt())
            .build();
    }

    private static Stay toStay(StayRequest stayRequest) {
        return Stay.builder()
            .placeName(stayRequest.placeName())
            .address(stayRequest.address())
            .startAt(stayRequest.startAt())
            .endAt(stayRequest.endAt())
            .build();
    }
}
