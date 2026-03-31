package com.auth.controller;

import com.auth.dto.TokenRefreshRequestDto;
import com.auth.dto.TokenRefreshResponseDto;
import com.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth controller api", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refresh(@RequestBody TokenRefreshRequestDto dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }
}
