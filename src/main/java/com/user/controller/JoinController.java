package com.user.controller;

import com.user.dto.request.JoinRequestDto;
import com.user.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "join controller api", description = "회원가입 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class JoinController {
    private final JoinService joinService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @PostMapping
    public ResponseEntity<?> join(@RequestBody JoinRequestDto joinRequestDto) {
        joinService.join(joinRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }
}
