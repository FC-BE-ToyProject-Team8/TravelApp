package kr.co.fastcampus.travel.domain.secure.repository;

import kr.co.fastcampus.travel.common.secure.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {

}
