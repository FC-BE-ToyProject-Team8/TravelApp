package kr.co.fastcampus.travel.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import kr.co.fastcampus.travel.common.exception.InvalidArgumentException;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.member.service.dto.request.MemberSaveDto;
import kr.co.fastcampus.travel.domain.member.service.dto.response.MemberDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 - 이메일 중복")
    void save_failureDuplicateEmail() {
        // given
        MemberSaveDto dto = new MemberSaveDto(
                "email",
                "name",
                "nickname",
                "password"
        );

        given(memberRepository.findByEmail(dto.email()))
                .willReturn(Optional.of(Member.builder().build()));

        // when
        // then
        assertThatThrownBy(() -> memberService.save(dto))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("회원가입 - 닉네임 중복")
    void save_failureDuplicateNickname() {
        // given
        MemberSaveDto dto = new MemberSaveDto(
                "email",
                "name",
                "nickname",
                "password"
        );

        given(memberRepository.findByEmail(dto.email()))
                .willReturn(Optional.empty());
        given(memberRepository.findByNickname(dto.nickname()))
                .willReturn(Optional.of(Member.builder().build()));

        // when
        // then
        assertThatThrownBy(() -> memberService.save(dto))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("이미 존재하는 닉네임입니다.");
    }

    @Test
    @DisplayName("회원가입")
    void saveMember() {
        // given
        MemberSaveDto dto = new MemberSaveDto(
                "email",
                "name",
                "nickname",
                "password"
        );
        String encodePassword = "encodedPassword";

        given(memberRepository.findByEmail(dto.email()))
                .willReturn(Optional.empty());
        given(memberRepository.findByNickname(dto.nickname()))
                .willReturn(Optional.empty());
        given(passwordEncoder.encode(dto.password()))
                .willReturn(encodePassword);
        given(memberRepository.save(any(Member.class)))
                .willReturn(Member.builder().build());

        // when
        MemberDto result = memberService.save(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(dto.email());
        assertThat(result.name()).isEqualTo(dto.name());
        assertThat(result.nickname()).isEqualTo(dto.nickname());
        verify(memberRepository, atLeastOnce()).save(any(Member.class));
    }
}
