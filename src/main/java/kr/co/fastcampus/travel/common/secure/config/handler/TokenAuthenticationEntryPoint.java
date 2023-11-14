package kr.co.fastcampus.travel.common.secure.config.handler;

import static kr.co.fastcampus.travel.common.secure.config.SecurityFilter.TOKEN_EXPIRED;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        Object errorMessage = request.getAttribute(TOKEN_EXPIRED);

        if (Objects.isNull(errorMessage)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        sendErrorResponse(response, errorMessage.toString());
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage)
            throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorMessage);
    }
}
