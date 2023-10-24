package kr.co.fastcampus.travel.controller.util;

import java.time.LocalDate;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Trip;

public class TravelDtoConverter {

    public static TripResponse toTripResponse(Trip trip) {
        return TripResponse.builder()
            .id(trip.getId())
            .name(trip.getName())
            .startDate(trip.getStartDate().toString())
            .endDate(trip.getEndDate().toString())
            .isForeign(trip.isForeign())
            .build();
    }

    public static Trip toTrip(TripRequest request) {
        return Trip.builder()
            .name(request.name())
            .startDate(LocalDate.parse(request.startDate()))
            .endDate(LocalDate.parse(request.endDate()))
            .isForeign(request.isForeign())
            .build();
    }
}
