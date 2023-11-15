package kr.co.fastcampus.travel.domain.trip.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.trip.controller.dto.TripDtoMapper;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripUpdateRequest;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
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
    private final TripDtoMapper mapper;

    @GetMapping
    @Operation(summary = "여행 목록")
    public ResponseBody<List<TripSummaryResponse>> getTripList() {
        var response = tripService.findAllTrips();
        return ResponseBody.ok(mapper.of(response));
    }

    @PostMapping
    @Operation(summary = "여행 등록")
    public ResponseBody<TripSummaryResponse> addTrip(
        @Valid @RequestBody TripSaveRequest request,
        Principal principal
    ) {
        String memberEmail = principal.getName();
        var response = tripService.addTrip(mapper.of(request), memberEmail);
        return ResponseBody.ok(mapper.of(response));
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "여정을 포함한 여행 조회")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long tripId) {
        var response = tripService.findTripItineraryById(tripId);
        return ResponseBody.ok(mapper.of(response));
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "여행 수정")
    public ResponseBody<TripSummaryResponse> editTrip(
        @PathVariable Long tripId,
        @Valid @RequestBody TripUpdateRequest request
    ) {
        var response = tripService.editTrip(tripId, mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "여행 삭제")
    public ResponseBody<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseBody.ok();
    }
}
