package kr.co.fastcampus.travel.domain.comment.service;

import static kr.co.fastcampus.travel.common.MemberTestUtils.createMember;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createComment;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentSaveDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createCommentUpdateDto;
import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TripService tripService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private CommentService commentService;


    @Test
    @DisplayName("댓글 달기 - 성공")
    void addComment() {
        // given
        Trip trip = createTrip();
        Member member = createMember();
        Comment comment = createComment(trip, member);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        CommentSaveDto request = createCommentSaveDto();

        // When
        CommentInfoDto result = commentService.addComment(trip.getId(), member.getEmail(), request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).isEqualTo(request.content());
        assertThat(result.member().email()).isEqualTo(comment.getMember().getEmail());
        assertThat(result.trip().name()).isEqualTo(comment.getTrip().getName());
    }

    @Test
    @DisplayName("댓글 수정 - 성공")
    void editComment() {
        // given
        Long commentId = -1L;
        Trip trip = createTrip();
        Member member = createMember();
        Comment comment = createComment(trip, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        CommentUpdateDto request = createCommentUpdateDto();

        // when
        CommentInfoDto updateResult =
            commentService.editComment(commentId, member.getEmail(), request);

        // then
        assertThat(updateResult).isNotNull();
        assertThat(updateResult.content()).isEqualTo(request.content());
        assertThat(updateResult.member().email()).isEqualTo(comment.getMember().getEmail());
        assertThat(updateResult.trip().name()).isEqualTo(comment.getTrip().getName());
    }

    @Test
    @DisplayName("댓글 수정 - 실패 (다른 유저)")
    void editCommentByDifferentMember() {
        // given
        Long commentId = -1L;
        String newMemberEmail = "otherEmail";
        Trip trip = createTrip();
        Member member = createMember();
        Comment comment = createComment(trip, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        CommentUpdateDto request = createCommentUpdateDto();

        // when, then
        assertThatThrownBy(() ->
            commentService.editComment(commentId, newMemberEmail, request))
            .isInstanceOf(CommentMemberMismatchException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 성공")
    void deleteComment(){
        // given
        Long commentId = -1L;
        Trip trip = createTrip();
        Member member = createMember();
        Comment comment = createComment(trip, member);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        commentService.deleteById(commentId, member.getEmail());

        // then
        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("댓글 삭제 - 실패 (댓글이 없는 경우)")
    void deleteCommentNotFound() {
        // given
        Long commentId = 1L;
        Member member = createMember();
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() ->
            commentService.deleteById(commentId, member.getEmail()))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
