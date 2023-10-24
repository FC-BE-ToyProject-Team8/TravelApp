package kr.co.fastcampus.travel.controller.util;

import java.time.LocalDate;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Trip;

public class TravelDtoConverter {

    public static TripResponse toTripResponse(Trip trip) {
        return new TripResponse(
            trip.getId(),
            trip.getName(),
            trip.getStartDate().toString(),
            trip.getEndDate().toString(),
            trip.isForeign()
        );
    }

    public static Trip toTrip(TripRequest request) {
        return new Trip(
            request.name(),
            LocalDate.parse(request.startDate()),
            LocalDate.parse(request.endDate()),
            request.isForeign()
        );
    }
}
