package kr.co.fastcampus.travel.domain.member.repository;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
}
