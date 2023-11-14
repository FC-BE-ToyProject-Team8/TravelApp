package kr.co.fastcampus.travel.domain.placesearch.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SameName(
    String keyword,
    List<String> region,
    @JsonProperty("selected_region")
    String selectedRegion
) {

}
