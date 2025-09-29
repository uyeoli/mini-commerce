package com.jwt;


public class JwtUtil {
    private final String key;
    private final long accessTokenExpTime;

    public JwtUtil(String key, long accessTokenExpTime) {
        this.key = key;
        this.accessTokenExpTime = accessTokenExpTime;
    }
}
