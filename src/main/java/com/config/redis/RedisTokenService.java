package com.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long REFRESH_TOKEN_TTL_DAYS = 60;
    private static final String KEY_PREFIX = "refresh:";

    public void saveRefreshToken(Long memberId, String refreshToken) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + memberId,
                refreshToken,
                REFRESH_TOKEN_TTL_DAYS,
                TimeUnit.DAYS
        );
    }

    public String getRefreshToken(Long memberId) {
        return (String) redisTemplate.opsForValue().get(KEY_PREFIX + memberId);
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(KEY_PREFIX + memberId);
    }
}
