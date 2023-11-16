package kr.co.fastcampus.travel.common.secure.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import kr.co.fastcampus.travel.common.exception.TokenExpireException;
import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class SecurityFilter implements Filter {

    public static final String TOKEN_EXPIRED = "tokenExpired";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtProvider jwtProvider;

    private static String getAuthorization(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = getAuthorization(httpServletRequest);
        String token = jwtProvider.resolveToken(authorization);
        try {
            if (isValidToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpireException e) {
            request.setAttribute(TOKEN_EXPIRED, e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        return StringUtils.hasText(token) && jwtProvider.validate(token);
    }
}
