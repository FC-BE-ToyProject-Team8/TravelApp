package kr.co.fastcampus.travel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import kr.co.fastcampus.travel.controller.request.ItineraryRequest;
import kr.co.fastcampus.travel.entity.Itinerary;
import kr.co.fastcampus.travel.entity.Lodge;
import kr.co.fastcampus.travel.entity.Route;
import kr.co.fastcampus.travel.entity.Stay;
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
    @DisplayName("여정 수정")
    void editItinerary() {
        //given
        Long id = 1L;
        Lodge lodge = Lodge.builder()
            .placeName("호텔")
            .address("부산 @@@")
            .checkOutAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkInAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();

        Stay stay = Stay.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        Route route = Route.builder()
            .transportation("지하철")
            .departurePlaceName("우리집")
            .departureAddress("서울")
            .destinationPlaceName("해운대")
            .destinationAddress("부산")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        Itinerary givenItinerary = Itinerary.builder()
            .stay(stay)
            .lodge(lodge)
            .route(route)
            .build();

        given(itineraryRepository.findById(id)).willReturn(Optional.of(givenItinerary));
        given(itineraryRepository.save(any())).willAnswer(
            invocation -> invocation.getArguments()[0]);

        Lodge lodge2 = Lodge.builder()
            .placeName("글램핑")
            .address("부산 @@@")
            .checkInAt(LocalDateTime.of(2023, 1, 1, 15, 00))
            .checkOutAt(LocalDateTime.of(2023, 1, 2, 11, 00))
            .build();

        Stay stay2 = Stay.builder()
            .placeName("한국")
            .address("대한민국")
            .startAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .endAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        Route route2 = Route.builder()
            .transportation("지하철")
            .departurePlaceName("우리집")
            .departureAddress("서울")
            .destinationPlaceName("해운대")
            .destinationAddress("부산")
            .departureAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .arriveAt(LocalDateTime.of(2023, 1, 1, 11, 30, 30))
            .build();

        ItineraryRequest request = ItineraryRequest.builder()
            .lodge(lodge2)
            .route(route2)
            .stay(stay2)
            .build();

        //when
        Itinerary editItinerary = itineraryService.editItinerary(id, request);

        //then
        assertThat(editItinerary.getLodge().getPlaceName()).isEqualTo("글램핑");
        assertThat(editItinerary.getLodge().getAddress()).isEqualTo("부산 @@@");
        assertThat(editItinerary.getLodge().getCheckInAt()).isEqualTo("2023-01-01T15:00:00");
        assertThat(editItinerary.getLodge().getCheckOutAt()).isEqualTo("2023-01-02T11:00");
    }

    @Test
    @DisplayName("여정 수정시 해당 여정이 존재하지 않는 경우")
    void editNotExistItineraryThenThrowException() {
        //given
        Long noExistId = 1L;
        given(itineraryRepository.findById(noExistId))
            .willReturn(Optional.empty());

        ItineraryRequest request = ItineraryRequest.builder().build();

        //when
        //then
        assertThatThrownBy(() -> itineraryService.editItinerary(noExistId, request));
    }
}
