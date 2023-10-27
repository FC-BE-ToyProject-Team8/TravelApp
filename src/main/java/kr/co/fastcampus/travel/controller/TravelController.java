package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toItineraryResponse;
import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripResponse;
import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripSummaryResponse;
import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripSummaryResponses;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.controller.response.TripSummaryResponse;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.service.ItineraryService;
import kr.co.fastcampus.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TravelController {

    private final TripService tripService;

    private final ItineraryService itineraryService;

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

    @PutMapping("/itineraries/{id}")
    @Operation(summary = "여정 수정")
    public ResponseBody<ItineraryResponse> editItinerary(
        @PathVariable Long id,
        @Valid @RequestBody ItineraryRequest itineraryRequest
    ) {
        Itinerary itinerary = itineraryService.editItinerary(id, itineraryRequest);
        ItineraryResponse response = toItineraryResponse(itinerary);
        return ResponseBody.ok(response);
    }

    @GetMapping("/trips")
    @Operation(summary = "여행 목록")
    public ResponseBody<List<TripSummaryResponse>> getTripList() {
        List<Trip> trips = tripService.findAllTrips();
        return ResponseBody.ok(toTripSummaryResponses(trips));
    }

    @PostMapping("/trips/{id}/itineraries")
    @Operation(summary = "여정 복수 등록")
    public ResponseBody<TripResponse> addItineraries(

        @PathVariable Long id, @Valid @RequestBody List<ItineraryRequest> request
    ) {
        Trip trip = tripService.findById(id);
        itineraryService.addItineraries(trip, request);
        return ResponseBody.ok(toTripResponse(tripService.findTripById(id)));
    }

    @PutMapping("/trips/{tripId}")
    @Operation(summary = "여행 수정")
    public ResponseBody<TripSummaryResponse> editTrip(
        @PathVariable Long tripId,
        @Valid @RequestBody TripRequest request
    ) {
        Trip trip = tripService.editTrip(tripId, request);
        TripSummaryResponse response = toTripSummaryResponse(trip);
        return ResponseBody.ok(response);
    }
}
