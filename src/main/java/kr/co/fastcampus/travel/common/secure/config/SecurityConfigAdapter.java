package kr.co.fastcampus.travel.common.secure.config;

import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class SecurityConfigAdapter
        extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>
{

    private final JwtProvider jwtProvider;

    @Override
    public void configure(HttpSecurity builder) {
        builder.addFilterBefore(
                new SecurityFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter.class
        );
    }
}
