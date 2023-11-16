package kr.co.fastcampus.travel.domain.secure.service;

import java.util.List;
import kr.co.fastcampus.travel.common.exception.InvalidArgumentException;
import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import kr.co.fastcampus.travel.common.secure.domain.PrincipalDetails;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.secure.service.reqeust.LoginDto;
import kr.co.fastcampus.travel.domain.secure.service.response.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SecurityService implements UserDetailsService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public TokenDto login(LoginDto dto) {
        var member = memberService.findMemberByEmail(dto.email());
        matchingPassword(dto.password(), member.getPassword());
        var token = jwtProvider.generateToken(member.getEmail(), member.getRole().name());
        return TokenDto.from(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var member = memberService.findMemberByEmail(username);
        List<SimpleGrantedAuthority> grantedAuthorities
            = List.of(new SimpleGrantedAuthority(member.getRole().name()));
        return new PrincipalDetails(member.getEmail(), grantedAuthorities);
    }

    private void matchingPassword(String password, String encodePassword) {
        if (!isMatchingPassword(password, encodePassword)) {
            throw new InvalidArgumentException("잘못된 비밀번호 입니다.");
        }
    }

    private boolean isMatchingPassword(String password, String encodePassword) {
        return passwordEncoder.matches(password, encodePassword);
    }
}
