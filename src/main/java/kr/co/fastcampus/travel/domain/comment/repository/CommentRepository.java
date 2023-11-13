package kr.co.fastcampus.travel.domain.comment.repository;

import kr.co.fastcampus.travel.domain.comment.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
