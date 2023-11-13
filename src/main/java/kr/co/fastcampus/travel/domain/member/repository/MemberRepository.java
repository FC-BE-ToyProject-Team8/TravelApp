package kr.co.fastcampus.travel.domain.member.repository;

import kr.co.fastcampus.travel.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
