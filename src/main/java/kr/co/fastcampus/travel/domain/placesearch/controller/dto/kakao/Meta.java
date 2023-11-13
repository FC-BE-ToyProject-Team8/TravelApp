package kr.co.fastcampus.travel.domain.placesearch.controller.dto.kakao;


import com.fasterxml.jackson.annotation.JsonProperty;

public record Meta(
    @JsonProperty("is_end")
    boolean isEnd,
    @JsonProperty("pageable_count")
    int pageableCount,
    @JsonProperty("total_count")
    int totalCount,
    @JsonProperty("same_name")
    SameName sameName
) {

}
