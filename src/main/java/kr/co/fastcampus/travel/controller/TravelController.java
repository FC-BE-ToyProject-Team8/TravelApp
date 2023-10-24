package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.controller.util.EntityToResponseConverter.convertTripToResponse;

import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TravelController {

    private final TripService tripService;

    @GetMapping("/trips/{id}")
    public ResponseBody<TripResponse> getTrip(@PathVariable Long id) {
        Trip trip = tripService.findTripById(id);
        return ResponseBody.ok(convertTripToResponse(trip));
    }
}
