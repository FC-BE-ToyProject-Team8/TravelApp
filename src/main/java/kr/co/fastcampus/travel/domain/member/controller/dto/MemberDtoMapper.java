package kr.co.fastcampus.travel.domain.member.controller.dto;

import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.member.controller.dto.response.MemberResponse;
import kr.co.fastcampus.travel.domain.member.service.dto.request.MemberSaveDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MemberDtoMapper {

    // reqeust
    MemberSaveDto of(MemberSaveRequest request);

    // response
    MemberResponse of(MemberSaveDto dto);
}
