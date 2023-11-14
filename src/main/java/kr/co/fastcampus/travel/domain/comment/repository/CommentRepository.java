package kr.co.fastcampus.travel.domain.comment.repository;

import java.util.Optional;
import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Optional<Member> findByEmail(String email);
}
