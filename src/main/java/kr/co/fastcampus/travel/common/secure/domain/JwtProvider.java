package kr.co.fastcampus.travel.common.secure.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import kr.co.fastcampus.travel.common.exception.TokenExpireException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtProvider {

    private static final int MILLISECONDS_TO_SECONDS = 1000;
    private static final int TOKEN_REFRESH_INTERVAL = MILLISECONDS_TO_SECONDS * 24;
    private static final String AUTHORITIES_KEY = "role";

    private final Key key;
    private final String grantType;
    private final long accessTokenExpiredTime;
    private final long refreshTokenExpiredTime;

    public JwtProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.grand-type}") String grantType,
        @Value("${jwt.token-validate-in-seconds}") long tokenValidateSeconds
    ) {
        this.key = getSecretKey(secretKey);
        this.grantType = grantType;
        this.accessTokenExpiredTime = tokenValidateSeconds * MILLISECONDS_TO_SECONDS;
        this.refreshTokenExpiredTime = tokenValidateSeconds * TOKEN_REFRESH_INTERVAL;
    }

    private static Collection<? extends GrantedAuthority> getGrantedAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    private SecretKey getSecretKey(String secretKey) {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(String email, String role) {
        String accessToken = createToken(email, role, accessTokenExpiredTime);
        String refreshToken = createToken(email, role, refreshTokenExpiredTime);

        return Token.builder()
            .email(email)
            .role(role)
            .grantType(grantType)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(grantType)) {
            return token.substring(grantType.length() + 1);
        }
        return null;
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 입니다.");
            throw new TokenExpireException();
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Collection<? extends GrantedAuthority> grantedAuthorities = getGrantedAuthorities(claims);
        PrincipalDetails principal = new PrincipalDetails(claims.getSubject(), grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(principal, token, grantedAuthorities);
    }

    private String createToken(String email, String role, long expiredTime) {
        return Jwts.builder()
            .setSubject(email)
            .claim(AUTHORITIES_KEY, role)
            .setExpiration(new Date(System.currentTimeMillis() + expiredTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims getClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
