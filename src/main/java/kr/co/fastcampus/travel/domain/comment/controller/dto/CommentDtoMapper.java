package kr.co.fastcampus.travel.domain.comment.controller.dto;

import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentSaveRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.request.CommentUpdateRequest;
import kr.co.fastcampus.travel.domain.comment.controller.dto.response.CommentResponse;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentSaveDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentUpdateDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.response.CommentInfoDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CommentDtoMapper {

    //request
    CommentSaveDto of(CommentSaveRequest request);
    CommentUpdateDto of(CommentUpdateRequest request);

    //response
    CommentResponse of(CommentInfoDto dto);
}
