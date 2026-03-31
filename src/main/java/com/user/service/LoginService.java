package com.user.service;

import com.config.redis.RedisTokenService;
import com.jwt.Jwt;
import com.jwt.JwtProvider;
import com.user.dto.request.LoginRequestDto;
import com.user.dto.response.LoginResponseDto;
import com.user.entity.Member;
import com.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByLoginId(loginRequestDto.getLoginId());

        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getId());
        claims.put("loginId", member.getLoginId());
        claims.put("name", member.getName());
        claims.put("email", member.getEmail());

        Jwt jwt = jwtProvider.createJwt(claims);
        redisTokenService.saveRefreshToken(member.getId(), jwt.getRefreshToken());

        // 4. 응답 DTO 생성
        return LoginResponseDto.builder()
                .memberId(member.getId())
                .loginId(member.getLoginId())
                .name(member.getName())
                .email(member.getEmail())
                .accessToken(jwt.getAccessToken())
                .refreshToken(jwt.getRefreshToken())
                .message("로그인에 성공했습니다.")
                .build();
    }
}