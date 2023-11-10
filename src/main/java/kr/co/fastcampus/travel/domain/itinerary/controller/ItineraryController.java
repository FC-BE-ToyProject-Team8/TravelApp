package kr.co.fastcampus.travel.domain.itinerary.controller;

import static kr.co.fastcampus.travel.domain.itinerary.controller.util.ItineraryDtoConverter.toItineraryResponse;
import static kr.co.fastcampus.travel.domain.trip.controller.util.TripDtoConverter.toTripResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.trip.controller.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.itinerary.service.ItineraryService;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final TripService tripService;
    private final ItineraryService itineraryService;

    @PostMapping
    @Operation(summary = "여정 복수 등록")
    public ResponseBody<TripResponse> addItineraries(
        @RequestParam Long tripId,
        @Valid @RequestBody List<ItineraryRequest> request
    ) {
        Trip trip = tripService.findById(tripId);
        itineraryService.addItineraries(trip, request);
        return ResponseBody.ok(toTripResponse(tripService.findTripById(tripId)));
    }

    @PutMapping("/{itineraryId}")
    @Operation(summary = "여정 수정")
    public ResponseBody<ItineraryResponse> editItinerary(
        @PathVariable Long itineraryId,
        @Valid @RequestBody ItineraryRequest itineraryRequest
    ) {
        Itinerary itinerary = itineraryService.editItinerary(itineraryId, itineraryRequest);
        ItineraryResponse response = toItineraryResponse(itinerary);
        return ResponseBody.ok(response);
    }

    @DeleteMapping("/{itineraryId}")
    @Operation(summary = "여정 삭제")
    public ResponseBody<Void> deleteItinerary(@PathVariable Long itineraryId) {
        itineraryService.deleteById(itineraryId);
        return ResponseBody.ok();
    }
}
