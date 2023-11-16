package kr.co.fastcampus.travel.common.secure.config;

import io.swagger.v3.oas.models.PathItem.HttpMethod;
import java.util.Arrays;
import kr.co.fastcampus.travel.common.secure.config.handler.TokenAccessDeniedHandler;
import kr.co.fastcampus.travel.common.secure.config.handler.TokenAuthenticationEntryPoint;
import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private static final String[] WHITELIST_FOR_ALL_METHOD = {
        "/api/login", "/api/signup", "/api/reissue",
        "/api/search-place", "/api/trips", "/api/trips/search-by-trip-name",
        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
        "/api/trips/search-by-nickname"
    };
    private static final String[] WHITELIST_FOR_GET_METHOD = {
        "/api/trips", "/api/trips/{tripId}", "/api/search-place", "/api/trips/search-by-trip-name"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .headers(header -> header.frameOptions(FrameOptionsConfig::disable).disable())
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    Arrays.stream(WHITELIST_FOR_ALL_METHOD)
                        .map(AntPathRequestMatcher::new)
                        .toArray(AntPathRequestMatcher[]::new)
                ).permitAll()
                .requestMatchers(
                    Arrays.stream(WHITELIST_FOR_GET_METHOD)
                        .map(
                            whitelist -> new AntPathRequestMatcher(whitelist, HttpMethod.GET.name())
                        )
                        .toArray(AntPathRequestMatcher[]::new)
                ).permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .anyRequest().authenticated())
        ;

        http
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(new TokenAccessDeniedHandler())
                .authenticationEntryPoint(new TokenAuthenticationEntryPoint())
            )
            .apply(new SecurityConfigAdapter(jwtProvider));

        return http.getOrBuild();
    }
}
