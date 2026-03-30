package com.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long memberId;
    private String loginId;
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;
    private String message;
}