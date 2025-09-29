package com.user.service;

import com.user.dto.JoinRequestDto;
import com.user.entity.Member;
import com.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final MemberRepository memberRepository;

    @Transactional
    public void join(JoinRequestDto joinRequestDto) {
        validateDuplicateLoginId(joinRequestDto.getLoginId());

        Member member = joinRequestDto.toEntity();
        memberRepository.join(member);
    }

    private void validateDuplicateLoginId(String loginId) {
        try {
            memberRepository.findByLoginId(loginId);
            throw new IllegalStateException("이미 존재하는 로그인 ID입니다.");
        } catch (jakarta.persistence.NoResultException e) {
            // 중복되지 않음 - 정상
        }
    }
}
