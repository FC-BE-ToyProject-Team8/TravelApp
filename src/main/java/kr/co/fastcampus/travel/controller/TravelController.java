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

    @GetMapping("/trips/{tripId}")
    @Operation(summary = "여정을 포함한 여행 조회")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long tripId) {
        Trip trip = tripService.findTripById(tripId);
        return ResponseBody.ok(toTripResponse(trip));
    }

    @PutMapping("/itineraries/{itineraryId}")
    @Operation(summary = "여정 수정")
    public ResponseBody<ItineraryResponse> editItinerary(
        @PathVariable Long itineraryId,
        @Valid @RequestBody ItineraryRequest itineraryRequest
    ) {
        Itinerary itinerary = itineraryService.editItinerary(itineraryId, itineraryRequest);
        ItineraryResponse response = toItineraryResponse(itinerary);
        return ResponseBody.ok(response);
    }

    @GetMapping("/trips")
    @Operation(summary = "여행 목록")
    public ResponseBody<List<TripSummaryResponse>> getTripList() {
        List<Trip> trips = tripService.findAllTrips();
        return ResponseBody.ok(toTripSummaryResponses(trips));
    }

    @PostMapping("/trips/{tripId}/itineraries")
    @Operation(summary = "여정 복수 등록")
    public ResponseBody<TripResponse> addItineraries(
        @PathVariable Long tripId,
        @Valid @RequestBody List<ItineraryRequest> request
    ) {
        Trip trip = tripService.findById(tripId);
        itineraryService.addItineraries(trip, request);
        return ResponseBody.ok(toTripResponse(tripService.findTripById(tripId)));
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

    @DeleteMapping("/trips/{tripId}")
    @Operation(summary = "여행 삭제")
    public ResponseBody<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseBody.ok();
    }

    @DeleteMapping("/itineraries/{itineraryId}")
    @Operation(summary = "여정 삭제")
    public ResponseBody<Void> deleteItinerary(@PathVariable Long itineraryId) {
        itineraryService.deleteById(itineraryId);
        return ResponseBody.ok();
    }
}
