package kr.co.fastcampus.travel.domain.itinerary.service;

import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItineraryRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createLodgeRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRoute;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRouteRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createStayRequest;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.LodgeRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.RouteRequest;
import kr.co.fastcampus.travel.domain.itinerary.controller.request.StayRequest;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    @Test
    @DisplayName("여정 데이터 전체 입력된 경우 수정")
    void editAllItinerary() {
        // given
        Long id = -1L;
        Trip trip = createTrip();
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
        Trip trip = createTrip();
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

    @Test
    @DisplayName("여정 복수 등록")
    void addItineraries() {
        // given
        Trip trip = createTrip();
        List<ItineraryRequest> requests = IntStream.range(0, 3)
            .mapToObj(i -> createItineraryRequest())
            .toList();

        //when
        Trip returnedTrip = itineraryService.addItineraries(trip, requests);

        //then
        assertThat(returnedTrip).isNotNull();
        assertThat(returnedTrip.getItineraries().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("여정 복수 등록 실패")
    void addItineraries_fail() {
        // given
        Trip trip = createTrip();
        List<ItineraryRequest> requests = IntStream.range(0, 3)
            .mapToObj(i -> createItineraryRequest())
            .toList();
        Trip otherTrip = createTrip();

        //when
        Trip returnedTrip = itineraryService.addItineraries(otherTrip, requests);

        //then
        assertThat(trip).isNotEqualTo(returnedTrip);
        assertThat(trip.getItineraries().size()).isNotEqualTo(returnedTrip.getItineraries().size());
    }

    @Test
    @DisplayName("여정 삭제")
    void deleteItinerary() {
        //given
        Trip trip = createTrip();
        Itinerary itinerary1 = createItinerary(trip);
        Itinerary itinerary2 = createItinerary(trip);
        when(itineraryRepository.findById(any())).thenReturn(Optional.of(itinerary2));

        //when
        itineraryService.deleteById(itinerary2.getId());

        //then
        verify(itineraryRepository).delete(itinerary2);
    }

    @Test
    @DisplayName("존재하지 않는 여정 삭제")
    void deleteNoneItinerary() {
        // given
        when(itineraryRepository.findById(any())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException e =
            assertThrows(EntityNotFoundException.class, () -> itineraryService.deleteById(2L));

        // then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 엔티티입니다.");
    }
}
