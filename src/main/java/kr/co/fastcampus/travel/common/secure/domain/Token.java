package kr.co.fastcampus.travel.common.secure.domain;

import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "token", timeToLive = 86400)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    private String email;
    private String role;

    private String refreshToken;

    @Transient
    private String grantType;

    @Transient
    private String accessToken;

    @Builder
    private Token(String email, String role, String grantType, String accessToken, String refreshToken) {
        this.email = email;
        this.role = role;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
