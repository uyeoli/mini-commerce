package com.jwt;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class Jwt {
    private String accessToken;
    private String refreshToken;


}
