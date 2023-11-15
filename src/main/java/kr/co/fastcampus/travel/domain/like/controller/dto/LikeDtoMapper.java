package kr.co.fastcampus.travel.domain.like.controller.dto;

import kr.co.fastcampus.travel.domain.like.controller.dto.request.LikeSaveRequest;
import kr.co.fastcampus.travel.domain.like.service.dto.LikeSaveDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface LikeDtoMapper {
    LikeSaveDto of(LikeSaveRequest request);
}
