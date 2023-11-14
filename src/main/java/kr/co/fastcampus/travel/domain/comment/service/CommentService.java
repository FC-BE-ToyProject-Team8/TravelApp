package kr.co.fastcampus.travel.domain.comment.service;

import kr.co.fastcampus.travel.common.exception.CommentMemberMismatchException;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.comment.repository.CommentRepository;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentSaveDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.request.CommentUpdateDto;
import kr.co.fastcampus.travel.domain.comment.service.dto.response.CommentInfoDto;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.service.TripService;
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
    private final TripService tripService;
    private final MemberService memberService;

    private Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public CommentInfoDto addComment(Long tripId, String memberEmail, CommentSaveDto dto) {
        Trip trip = tripService.findById(tripId);
        Member member = memberService.findMemberByEmail(memberEmail);

        Comment comment = dto.toEntity();
        comment.setMember(member);
        comment.setTrip(trip);

        Comment newComment = commentRepository.save(comment);
        return CommentInfoDto.from(newComment);
    }

    @Transactional
    public CommentInfoDto editComment(Long commentId, String memberEmail,
        CommentUpdateDto request) {
        Comment comment = findById(commentId);
        validateMemberMatch(memberEmail, comment);
        comment.update(request.content());
        return CommentInfoDto.from(comment);
    }

    public void deleteById(Long commentId, String memberEmail) {
        Comment comment = findById(commentId);
        validateMemberMatch(memberEmail, comment);
        commentRepository.delete(comment);
    }

    private void validateMemberMatch(String memberEmail, Comment comment) {
        if (!comment.getMember().getEmail().equals(memberEmail)) {
            throw new CommentMemberMismatchException();
        }
    }
}
