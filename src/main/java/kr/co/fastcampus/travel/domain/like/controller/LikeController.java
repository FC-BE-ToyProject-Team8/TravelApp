package kr.co.fastcampus.travel.domain.like.controller;

import java.security.Principal;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseBody<Void> likeTrip(@RequestParam("tripId") Long tripId, Principal principal) {
        String memberEmail = principal.getName();
        likeService.saveLike(tripId, memberEmail);
        return ResponseBody.ok();
    }
}
