package kr.co.fastcampus.travel.domain.secure.repository;

import java.util.Optional;
import kr.co.fastcampus.travel.common.secure.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {

    Optional<Token> findByEmail(String email);
}
