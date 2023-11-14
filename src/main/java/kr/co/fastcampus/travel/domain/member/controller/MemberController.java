package kr.co.fastcampus.travel.domain.member.controller;

import jakarta.validation.Valid;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.member.controller.dto.MemberDtoMapper;
import kr.co.fastcampus.travel.domain.member.controller.dto.request.MemberSaveRequest;
import kr.co.fastcampus.travel.domain.member.controller.dto.response.MemberSaveResponse;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberDtoMapper mapper;

    @PostMapping("/signup")
    public ResponseBody<MemberSaveResponse> signup(@RequestBody @Valid MemberSaveRequest request) {
        var response = memberService.save(mapper.of(request));
        return ResponseBody.ok(mapper.of(response));
    }
}
