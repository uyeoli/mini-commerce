package com.auth.dto;

import lombok.Getter;

@Getter
public class TokenRefreshRequestDto {
    private String refreshToken;
}
