package kr.co.fastcampus.travel;

import java.time.LocalDate;
import kr.co.fastcampus.travel.entity.Trip;

public class TravelUtils {
    public static Trip createTrip() {
        return Trip.builder()
            .name("name")
            .startDate(LocalDate.of(2023, 1, 1))
            .endDate(LocalDate.of(2023, 1, 7))
            .isForeign(true)
            .build();
    }
}
