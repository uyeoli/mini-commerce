package com.user.service;

import com.user.dto.request.JoinRequestDto;
import com.user.entity.Member;
import com.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void join(JoinRequestDto joinRequestDto) {
        validateDuplicateLoginId(joinRequestDto.getLoginId());
        String encodedPassword = encodePassword(joinRequestDto.getPassword());
        Member member = joinRequestDto.toEntity(encodedPassword);
        memberRepository.join(member);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateDuplicateLoginId(String loginId) {
        try {
            Member member = memberRepository.findByLoginId(loginId);
            if(member != null) {
                throw new IllegalStateException("이미 존재하는 로그인 ID입니다.");
            }
        } catch (jakarta.persistence.NoResultException e) {
            e.printStackTrace();
        }
    }
}
