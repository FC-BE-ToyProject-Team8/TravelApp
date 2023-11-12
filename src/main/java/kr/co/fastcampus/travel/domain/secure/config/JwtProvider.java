package kr.co.fastcampus.travel.domain.secure.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.secure.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int TOKEN_REFRESH_INTERVAL = MILLISECONDS_TO_SECONDS * 24;

    private final Key key;
    private final String grantType;
    private final long accessTokenExpiredTime;
    private final long refreshTokenExpiredTime;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.grand-type}") String grantType,
            @Value("${jwt.token-validate-in-seconds}") long tokenValidateSeconds
    ) {
        byte[] secretByteKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
        this.grantType = grantType;
        this.accessTokenExpiredTime = tokenValidateSeconds * MILLISECONDS_TO_SECONDS;
        this.refreshTokenExpiredTime = tokenValidateSeconds * TOKEN_REFRESH_INTERVAL;
    }

    public Token generateToken(Member member) {
        String accessToken = createToken(member, accessTokenExpiredTime);
        String refreshToken = createToken(member, refreshTokenExpiredTime);

        return Token.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String createToken(Member member, long expiredTime) {
        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("role", member.getRole().name())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
