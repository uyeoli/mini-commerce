package com.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtProvider {

    private final Key key;
    private final long accessTokenExpTime;

    public JwtProvider(String secret, long accessTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    public Jwt createJwt(Map<String, Object> claims) {
        String accessToken = createToken(claims, getExpireDateAccessToken());
        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("memberId", claims.get("memberId"));
        String refreshToken = createToken(refreshClaims, getExpireDateRefreshToken());
        return Jwt.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createAccessToken(Map<String, Object> claims) {
        return createToken(claims, getExpireDateAccessToken());
    }

    public String createToken(Map<String, Object> claims, Date expireDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Date getExpireDateAccessToken() {
        return new Date(System.currentTimeMillis() + accessTokenExpTime);
    }

    public Date getExpireDateRefreshToken() {
        long expireTimeMils = 1000L * 60 * 60 * 24 * 60;
        return new Date(System.currentTimeMillis() + expireTimeMils);
    }


}
