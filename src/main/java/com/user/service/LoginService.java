package com.user.service;

import com.jwt.Jwt;
import com.jwt.JwtProvider;
import com.user.dto.request.LoginRequestDto;
import com.user.dto.response.LoginResponseDto;
import com.user.entity.Member;
import com.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByLoginId(loginRequestDto.getLoginId());

        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (!member.getPassword().equals(loginRequestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", member.getLoginId());
        claims.put("name", member.getName());
        claims.put("email", member.getEmail());

        Jwt jwt = jwtProvider.createJwt(claims);

        // 4. 응답 DTO 생성
        return LoginResponseDto.builder()
                .accessToken(jwt.getAccessToken())
                .refreshToken(jwt.getRefreshToken())
                .message("로그인에 성공했습니다.")
                .build();
    }
}