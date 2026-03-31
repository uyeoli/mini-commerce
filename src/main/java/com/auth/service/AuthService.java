package com.auth.service;

import com.auth.dto.TokenRefreshRequestDto;
import com.auth.dto.TokenRefreshResponseDto;
import com.config.redis.RedisTokenService;
import com.jwt.JwtProvider;
import com.user.entity.Member;
import com.user.repository.MemberJpaRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisTokenService redisTokenService;
    private final MemberJpaRepository memberJpaRepository;

    public TokenRefreshResponseDto refresh(TokenRefreshRequestDto dto) {
        String requestToken = dto.getRefreshToken();

        if (!jwtProvider.validateToken(requestToken)) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 리프레시 토큰입니다.");
        }

        Claims claims = jwtProvider.getClaims(requestToken);
        Long memberId = ((Number) claims.get("memberId")).longValue();

        String storedToken = redisTokenService.getRefreshToken(memberId);
        if (storedToken == null || !storedToken.equals(requestToken)) {
            throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
        }

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("memberId", member.getId());
        newClaims.put("loginId", member.getLoginId());
        newClaims.put("name", member.getName());
        newClaims.put("email", member.getEmail());

        String newAccessToken = jwtProvider.createAccessToken(newClaims);

        return TokenRefreshResponseDto.builder()
                .accessToken(newAccessToken)
                .message("액세스 토큰이 재발급되었습니다.")
                .build();
    }
}
