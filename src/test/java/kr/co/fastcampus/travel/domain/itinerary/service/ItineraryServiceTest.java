package kr.co.fastcampus.travel.domain.itinerary.service;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createItineraryUpdateDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createLodgeUpdateDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRoute;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createRouteUpdateDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createStayUpdateDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTripWithMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.InvalidDateSequenceException;
import kr.co.fastcampus.travel.common.exception.MemberMismatchException;
import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
import kr.co.fastcampus.travel.domain.itinerary.entity.Route;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;
import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.ItineraryUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.LodgeUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.RouteUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.update.StayUpdateDto;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.response.ItineraryDto;
import kr.co.fastcampus.travel.domain.member.entity.Member;
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
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Itinerary givenItinerary = createItinerary(trip);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givenItinerary));

        RouteUpdateDto route = createRouteUpdateDto();
        LodgeUpdateDto lodge = createLodgeUpdateDto();
        StayUpdateDto stay = createStayUpdateDto();

        ItineraryUpdateDto request = createItineraryUpdateDto(route, lodge, stay);

        // when
        ItineraryDto editItinerary = itineraryService.editItinerary(id, member.getEmail(), request);

        // then
        assertThat(editItinerary).isNotNull();
        assertThat(editItinerary.lodge().placeName()).isEqualTo("장소 업데이트");
        assertThat(editItinerary.lodge().address()).isEqualTo("주소 업데이트");
        assertThat(editItinerary.lodge().checkInAt()).isEqualTo("2023-01-01T15:00:00");
        assertThat(editItinerary.lodge().checkOutAt()).isEqualTo("2023-01-02T11:00");
    }

    @Test
    @DisplayName("여정 route만 입력되어 있는 경우 수정")
    void editPartItinerary() {
        // given
        Long id = -1L;
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Route route = createRoute();
        Itinerary givneItinerary = createItinerary(trip, route, null, null);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givneItinerary));

        RouteUpdateDto editRouteRequest = createRouteUpdateDto();
        ItineraryUpdateDto request = createItineraryUpdateDto(editRouteRequest, null, null);

        // when
        ItineraryDto editItinerary = itineraryService.editItinerary(id, member.getEmail(), request);

        // then
        assertThat(editItinerary.route().transportation()).isEqualTo(Transportation.SUBWAY);
        assertThat(editItinerary.route().departurePlaceName()).isEqualTo("출발지 업데이트");
        assertThat(editItinerary.lodge()).isNull();
        assertThat(editItinerary.stay()).isNull();
    }

    @Test
    @DisplayName("여정 수정시 해당 여정이 존재하지 않는 경우")
    void editNotExistItineraryThenThrowException() {
        // given
        Long noExistId = 1L;
        Member member = createMember();
        given(itineraryRepository.findById(noExistId))
            .willReturn(Optional.empty());

        ItineraryUpdateDto request = createItineraryUpdateDto();

        // when , then
        assertThatThrownBy(
            () -> itineraryService.editItinerary(noExistId, member.getEmail(), request))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("여정 수정시 여행을 등록한 사람이 아닌 사람이 수정할 경우")
    void editItineraryByDifferentMember() {
        // given
        Long id = -1L;
        String newMemberEmail = "otherEmail";
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Itinerary givenItinerary = createItinerary(trip);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givenItinerary));

        RouteUpdateDto route = createRouteUpdateDto();
        LodgeUpdateDto lodge = createLodgeUpdateDto();
        StayUpdateDto stay = createStayUpdateDto();

        ItineraryUpdateDto request = createItineraryUpdateDto(route, lodge, stay);

        // when, then
        assertThatThrownBy(() ->
            itineraryService.editItinerary(id, newMemberEmail, request))
            .isInstanceOf(MemberMismatchException.class);
    }

    @Test
    @DisplayName("여정 삭제")
    void deleteItinerary() {
        //given
        Trip trip = createTrip();
        Itinerary itinerary = createItinerary(trip);
        when(itineraryRepository.findById(any())).thenReturn(Optional.of(itinerary));

        //when
        itineraryService.deleteById(itinerary.getId());

        //then
        verify(itineraryRepository).delete(itinerary);
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

    @Test
    @DisplayName("여정 수정 시 Lodge 종료일시가 시작일시보다 앞서면 예외")
    void editItinerary_Lodge_InvalidDateSequence() {
        // given
        Long id = -1L;
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Route route = createRoute();
        Itinerary givneItinerary = createItinerary(trip, route, null, null);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givneItinerary));

        LodgeUpdateDto lodgeUpdateDto = new LodgeUpdateDto("이름",
            "주소",
            LocalDateTime.of(2010, 1, 2, 0, 0),
            LocalDateTime.of(2010, 1, 1, 0, 0)
        );
        ItineraryUpdateDto request = createItineraryUpdateDto(null, lodgeUpdateDto, null);

        // when, then
        assertThatThrownBy(() -> itineraryService.editItinerary(id, member.getEmail(), request))
            .isInstanceOf(InvalidDateSequenceException.class);
    }

    @Test
    @DisplayName("여정 수정 시 Route 종료일시가 시작일시보다 앞서면 예외")
    void editItinerary_Route_InvalidDateSequence() {
        // given
        Long id = -1L;
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Route route = createRoute();
        Itinerary givneItinerary = createItinerary(trip, route, null, null);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givneItinerary));

        RouteUpdateDto routeUpdateDto = new RouteUpdateDto(Transportation.BUS,
            "출발지 이름",
            "출발지 주소",
            "도착지 이름",
            "도착지 주소",
            LocalDateTime.of(2010, 1, 2, 0, 0),
            LocalDateTime.of(2010, 1, 1, 0, 0)
        );
        ItineraryUpdateDto request = createItineraryUpdateDto(routeUpdateDto, null, null);

        // when, then
        assertThatThrownBy(() -> itineraryService.editItinerary(id, member.getEmail(), request))
            .isInstanceOf(InvalidDateSequenceException.class);
    }

    @Test
    @DisplayName("여정 수정 시 Stay 종료일시가 시작일시보다 앞서면 예외")
    void editItinerary_Stay_InvalidDateSequence() {
        // given
        Long id = -1L;
        Member member = createMember();
        Trip trip = createTripWithMember(member);
        Route route = createRoute();
        Itinerary givneItinerary = createItinerary(trip, route, null, null);
        given(itineraryRepository.findById(id)).willReturn(Optional.of(givneItinerary));

        StayUpdateDto stayUpdateDto = new StayUpdateDto("이름",
            "주소",
            LocalDateTime.of(2010, 1, 2, 0, 0),
            LocalDateTime.of(2010, 1, 1, 0, 0)
        );
        ItineraryUpdateDto request = createItineraryUpdateDto(null, null, stayUpdateDto);

        // when, then
        assertThatThrownBy(() -> itineraryService.editItinerary(id, member.getEmail(), request))
            .isInstanceOf(InvalidDateSequenceException.class);
    }
}
