package kr.co.fastcampus.travel.domain.itinerary.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.ItineraryDtoMapper;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerariesSaveRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.update.ItineraryUpdateRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.dto.response.ItineraryResponse;
import kr.co.fastcampus.travel.domain.itinerary.service.ItineraryService;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final TripService tripService;
    private final ItineraryService itineraryService;
    private final ItineraryDtoMapper mapper;

    @PostMapping
    @Operation(summary = "여정 복수 등록")
    public ResponseBody<List<ItineraryResponse>> saveItineraries(
        @RequestBody ItinerariesSaveRequest request
    ) {
        var response = tripService.addItineraries(request.tripId(), mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }

    @PutMapping("/{itineraryId}")
    @Operation(summary = "여정 수정")
    public ResponseBody<ItineraryResponse> editItinerary(
        @PathVariable Long itineraryId,
        @Valid @RequestBody ItineraryUpdateRequest request,
        Principal principal
    ) {
        String memberEmail = principal.getName();
        var response = itineraryService.editItinerary(itineraryId, memberEmail, mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }

    @DeleteMapping("/{itineraryId}")
    @Operation(summary = "여정 삭제")
    public ResponseBody<Void> deleteItinerary(@PathVariable Long itineraryId) {
        itineraryService.deleteById(itineraryId);
        return ResponseBody.ok();
    }
}
