package kr.co.fastcampus.travel.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.service.ItineraryService;
import kr.co.fastcampus.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripResponse;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TravelController {

    private final TripService tripService;

    private final ItineraryService itineraryService;

    @GetMapping("/trips/{id}")
    @Operation(summary = "여정을 포함한 여행 조회")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long id) {
        Trip trip = tripService.findTripById(id);
        return ResponseBody.ok(toTripResponse(trip));
    }

    @PostMapping("/trips/{id}/itineraries")
    @Operation(summary = "여정 복수 등록")
    public ResponseBody<TripResponse> addItineraries(@PathVariable Long id, @Valid @RequestBody List<ItineraryRequest> request) {
        Trip trip = itineraryService.addItineraries(id, request);
        return ResponseBody.ok(toTripResponse(trip));
    }
}
