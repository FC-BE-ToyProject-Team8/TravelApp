package kr.co.fastcampus.travel.domain.secure.controller;

import jakarta.validation.Valid;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.secure.controller.dto.SecurityDtoMapper;
import kr.co.fastcampus.travel.domain.secure.controller.dto.request.LoginReqeust;
import kr.co.fastcampus.travel.domain.secure.controller.dto.response.LoginResponse;
import kr.co.fastcampus.travel.domain.secure.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final SecurityDtoMapper mapper;

    @PostMapping("/login")
    public ResponseBody<LoginResponse> login(@RequestBody @Valid LoginReqeust reqeust) {
        var response = securityService.login(mapper.of(reqeust));
        return ResponseBody.ok(mapper.of(response));
    }
}
