package kr.co.fastcampus.travel.common.secure.config;

import kr.co.fastcampus.travel.common.secure.config.handler.TokenAccessDeniedHandler;
import kr.co.fastcampus.travel.common.secure.config.handler.TokenAuthenticationEntryPoint;
import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/login", HttpMethod.GET.name())
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/join", HttpMethod.POST.name())
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/reissue", HttpMethod.POST.name())
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

        return http.build();
    }
}
