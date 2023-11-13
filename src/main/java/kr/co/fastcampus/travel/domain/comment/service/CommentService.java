package kr.co.fastcampus.travel.domain.comment.service;

import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.comment.repository.CommentRepository;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentSaveDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.response.CommentInfoDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TripRepository tripRepository;

    public CommentInfoDto addComment(Long tripId, CommentSaveDto dto) {
//        tripRepository.findById(tripId).orElseThrow(EntityNotFoundException::new);

        var comment = commentRepository.save(dto.toEntity());
        return CommentInfoDto.from(comment);
    }
}
