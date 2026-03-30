package com.user.service;

import com.user.dto.request.MemberUpdateDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {

    @Transactional
    void updateMember(Long id, MemberUpdateDto dto);
}
