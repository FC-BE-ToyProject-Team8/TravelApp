package kr.co.fastcampus.travel;

import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;

import java.time.LocalDate;

public class testUtil {
    public static Trip createMockTrip() {
        return Trip.builder()
                .name("tripName")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .isForeign(true)
                .build();
    }
    public static ItineraryRequest createMockItineraryRequest(){
        return ItineraryRequest.builder().build();
    }
}
