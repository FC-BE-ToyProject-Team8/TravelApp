package kr.co.fastcampus.travel.domain.secure.service;

import static kr.co.fastcampus.travel.common.MemberTestUtils.EMAIL;
import static kr.co.fastcampus.travel.common.MemberTestUtils.PASSWORD;
import static kr.co.fastcampus.travel.common.MemberTestUtils.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import kr.co.fastcampus.travel.common.exception.InvalidArgumentException;
import kr.co.fastcampus.travel.common.secure.domain.JwtProvider;
import kr.co.fastcampus.travel.common.secure.domain.Token;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.service.MemberService;
import kr.co.fastcampus.travel.domain.secure.service.reqeust.LoginDto;
import kr.co.fastcampus.travel.domain.secure.service.response.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private SecurityService securityService;


    @Test
    @DisplayName("로그인 - 비밀번호 미일치")
    void login_notMatchingPassword() {
        // given
        LoginDto dto = new LoginDto(EMAIL, "otherPassword");
        Member member = createMember();
        given(memberService.findMemberByEmail(dto.email()))
            .willReturn(member);
        given(passwordEncoder.matches(dto.password(), member.getPassword()))
            .willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> securityService.login(dto))
            .isInstanceOf(InvalidArgumentException.class)
            .hasMessage("잘못된 비밀번호 입니다.");
    }

    @Test
    @DisplayName("로그인")
    void saveMember() {
        // given
        LoginDto dto = new LoginDto(EMAIL, PASSWORD);
        Member member = createMember();
        given(memberService.findMemberByEmail(dto.email()))
            .willReturn(member);
        given(passwordEncoder.matches(dto.password(), member.getPassword()))
            .willReturn(true);
        given(jwtProvider.generateToken(member.getEmail(), member.getRole().name()))
            .willReturn(Token.builder().build());

        // when
        TokenDto result = securityService.login(dto);

        // then
        assertThat(result).isNotNull();
    }
}
