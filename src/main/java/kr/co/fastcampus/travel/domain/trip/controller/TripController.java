package kr.co.fastcampus.travel.domain.trip.controller;

import static kr.co.fastcampus.travel.domain.trip.controller.util.TripDtoConverter.toTripResponse;
import static kr.co.fastcampus.travel.domain.trip.controller.util.TripDtoConverter.toTripSummaryResponse;
import static kr.co.fastcampus.travel.domain.trip.controller.util.TripDtoConverter.toTripSummaryResponses;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.itinerary.service.ItineraryService;
import kr.co.fastcampus.travel.domain.trip.controller.request.TripRequest;
import kr.co.fastcampus.travel.domain.trip.controller.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.response.TripSummaryResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final ItineraryService itineraryService;

    @GetMapping
    @Operation(summary = "여행 목록")
    public ResponseBody<List<TripSummaryResponse>> getTripList() {
        List<Trip> trips = tripService.findAllTrips();
        return ResponseBody.ok(toTripSummaryResponses(trips));
    }

    @PostMapping
    @Operation(summary = "여행 등록")
    public ResponseBody<TripSummaryResponse> addTrip(
        @Valid @RequestBody TripRequest request
    ) {
        Trip trip = tripService.addTrip(request);
        TripSummaryResponse response = toTripSummaryResponse(trip);
        return ResponseBody.ok(response);
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "여정을 포함한 여행 조회")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long tripId) {
        Trip trip = tripService.findTripById(tripId);
        return ResponseBody.ok(toTripResponse(trip));
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "여행 수정")
    public ResponseBody<TripSummaryResponse> editTrip(
        @PathVariable Long tripId,
        @Valid @RequestBody TripRequest request
    ) {
        Trip trip = tripService.editTrip(tripId, request);
        TripSummaryResponse response = toTripSummaryResponse(trip);
        return ResponseBody.ok(response);
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "여행 삭제")
    public ResponseBody<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseBody.ok();
    }
}
