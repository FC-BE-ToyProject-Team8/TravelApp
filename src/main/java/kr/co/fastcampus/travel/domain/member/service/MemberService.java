package kr.co.fastcampus.travel.domain.member.service;

import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.common.exception.InvalidArgumentException;
import kr.co.fastcampus.travel.common.exception.MemberNotFoundException;
import kr.co.fastcampus.travel.domain.member.entity.Member;
import kr.co.fastcampus.travel.domain.member.repository.MemberRepository;
import kr.co.fastcampus.travel.domain.member.service.dto.request.MemberSaveDto;
import kr.co.fastcampus.travel.domain.member.service.dto.response.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto save(MemberSaveDto dto) {
        validateSaveMember(dto);
        var encodePassword = passwordEncoder.encode(dto.password());
        Member member = dto.toEntity(encodePassword);
        memberRepository.save(member);
        return MemberDto.from(member);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(EntityNotFoundException::new);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
            .orElseThrow(MemberNotFoundException::new);
    }

    private void validateSaveMember(MemberSaveDto dto) {
        if (memberRepository.findByEmail(dto.email()).isPresent()) {
            throw new InvalidArgumentException("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByNickname(dto.nickname()).isPresent()) {
            throw new InvalidArgumentException("이미 존재하는 닉네임입니다.");
        }
    }
}
