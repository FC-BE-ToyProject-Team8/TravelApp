package kr.co.fastcampus.travel.domain.trip.controller.util;

import java.util.List;
import kr.co.fastcampus.travel.domain.itinerary.controller.util.ItineraryDtoConverter;
import kr.co.fastcampus.travel.domain.trip.controller.request.TripRequest;
import kr.co.fastcampus.travel.domain.trip.controller.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;

public class TripDtoConverter {

    private TripDtoConverter() {
    }

    public static TripResponse toTripResponse(Trip trip) {
        return TripResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startAt(trip.getStartDate())
            .endAt(trip.getEndDate())
            .isForeign(trip.isForeign())
            .itineraries(ItineraryDtoConverter.getItineraryResponses(trip))
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
            .map(TripDtoConverter::toTripSummaryResponse)
            .toList();
    }

    public static Trip toTrip(TripRequest request) {
        return Trip.builder()
            .name(request.name())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .isForeign(request.isForeign())
            .build();
    }
}
