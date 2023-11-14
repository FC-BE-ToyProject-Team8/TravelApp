package kr.co.fastcampus.travel.domain.secure.controller.dto;

import kr.co.fastcampus.travel.domain.secure.controller.dto.request.LoginReqeust;
import kr.co.fastcampus.travel.domain.secure.controller.dto.response.LoginResponse;
import kr.co.fastcampus.travel.domain.secure.service.reqeust.LoginDto;
import kr.co.fastcampus.travel.domain.secure.service.response.TokenDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SecurityDtoMapper {

    // request
    LoginDto of(LoginReqeust reqeust);

    // response
    LoginResponse of(TokenDto dto);
}
