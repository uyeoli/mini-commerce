package com.user.service;

import com.user.dto.request.MemberUpdateDto;
import com.user.entity.Member;
import com.user.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    @Transactional
    public void updateMember(Long id, MemberUpdateDto dto) {
        Member member = memberJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        member.update(dto.getName(), dto.getEmail(), dto.getPassword());
    }
}
