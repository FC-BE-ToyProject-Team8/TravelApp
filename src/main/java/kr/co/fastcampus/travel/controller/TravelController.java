package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripResponse;
import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripSummaryResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.controller.response.TripSummaryResponse;
import kr.co.fastcampus.travel.controller.util.TravelDtoConverter;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TravelController {

    private final TripService tripService;

    @PostMapping("/trips")
    @Operation(summary = "여행 등록")
    public ResponseBody<TripSummaryResponse> addTrip(
        @Valid @RequestBody TripRequest request
    ) {
        Trip trip = tripService.addTrip(request);
        TripSummaryResponse response = toTripSummaryResponse(trip);
        return ResponseBody.ok(response);
    }

    @GetMapping("/trips/{id}")
    @Operation(summary = "여정을 포함한 여행 조회")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long id) {
        Trip trip = tripService.findTripById(id);
        return ResponseBody.ok(toTripResponse(trip));
    }

    @GetMapping("/trips")
    @Operation(summary = "여행 목록")
    public ResponseBody<List<TripResponse>> getTripList() {
        List<Trip> trips = tripService.findAllTrips();
        List<TripResponse> response =
            trips.stream()
                .map(TravelDtoConverter::toTripResponse)
                .toList();
        return ResponseBody.ok(response);
    }
}
