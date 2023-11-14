package kr.co.fastcampus.travel.domain.placesearch.controller.dto;

import java.util.List;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao.Place;
import kr.co.fastcampus.travel.domain.placesearch.controller.dto.response.PlaceInfoResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PlaceSearchDtoMapper {

    @Mapping(source = "placeName", target = "name")
    @Mapping(source = "categoryGroupName", target = "category")
    @Mapping(target = "address", expression = "java(getAddress(place))")
    PlaceInfoResponse of(Place place);

    List<PlaceInfoResponse> of(List<Place> places);

    default String getAddress(Place place) {
        if (place.roadAddressName().isEmpty()) {
            return place.addressName();
        } else {
            return place.roadAddressName();
        }
    }
}
