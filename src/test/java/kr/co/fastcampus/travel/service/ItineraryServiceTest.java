package kr.co.fastcampus.travel.service;

import static kr.co.fastcampus.travel.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.TravelTestUtils.createItineraryRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createLodgeRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createMockTrip;
import static kr.co.fastcampus.travel.TravelTestUtils.createRoute;
import static kr.co.fastcampus.travel.TravelTestUtils.createRouteRequest;
import static kr.co.fastcampus.travel.TravelTestUtils.createStayRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.controller.request.RouteRequest;
import kr.co.fastcampus.travel.controller.request.StayRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.repository.ItineraryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    @Test
    @DisplayName("여정 데이터 전체 입력된 경우 수정")
    void editAllItinerary() {
        // given
        Long id = -1L;
        Trip trip = createMockTrip();
        Itinerary givenItinerary = createItinerary(trip);

        given(itineraryRepository.findById(id)).willReturn(Optional.of(givenItinerary));
        given(itineraryRepository.save(any())).willAnswer(
            invocation -> invocation.getArguments()[0]);

        RouteRequest route = createRouteRequest();
        LodgeRequest lodge = createLodgeRequest();
        StayRequest stay = createStayRequest();

        ItineraryRequest request = createItineraryRequest(route, lodge, stay);

        // when
        Itinerary editItinerary = itineraryService.editItinerary(id, request);

        // then
        assertThat(editItinerary.getLodge().getPlaceName()).isEqualTo("장소 업데이트");
        assertThat(editItinerary.getLodge().getAddress()).isEqualTo("주소 업데이트");
        assertThat(editItinerary.getLodge().getCheckInAt()).isEqualTo("2023-01-01T15:00:00");
        assertThat(editItinerary.getLodge().getCheckOutAt()).isEqualTo("2023-01-02T11:00");
    }

    @Test
    @DisplayName("여정 route만 입력되어 있는 경우 수정")
    void editPartItinerary() {
        // given
        Long id = -1L;
        Trip trip = createMockTrip();
        Route route = createRoute();
        Itinerary givneItinerary = createItinerary(trip, route, null, null);

        given(itineraryRepository.findById(id)).willReturn(Optional.of(givneItinerary));
        given(itineraryRepository.save(any())).willAnswer(
            invocation -> invocation.getArguments()[0]);

        RouteRequest editRouteRequest = createRouteRequest();
        ItineraryRequest request = createItineraryRequest(editRouteRequest, null, null);

        // when
        Itinerary editItinerary = itineraryService.editItinerary(id, request);

        // then
        assertThat(editItinerary.getRoute().getTransportation()).isEqualTo("이동수단 업데이트");
        assertThat(editItinerary.getRoute().getDeparturePlaceName()).isEqualTo("출발지 업데이트");
        assertThat(editItinerary.getLodge()).isNull();
        assertThat(editItinerary.getStay()).isNull();
    }

    @Test
    @DisplayName("여정 수정시 해당 여정이 존재하지 않는 경우")
    void editNotExistItineraryThenThrowException() {
        // given
        Long noExistId = 1L;
        given(itineraryRepository.findById(noExistId))
            .willReturn(Optional.empty());

        ItineraryRequest request = ItineraryRequest.builder().build();

        // when , then
        assertThatThrownBy(() -> itineraryService.editItinerary(noExistId, request))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
