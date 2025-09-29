package com.user.controller;

import com.user.dto.JoinRequestDto;
import com.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class JoinController {
    private final JoinService joinService;

    @PostMapping
    public ResponseEntity<?> join(@RequestBody JoinRequestDto joinRequestDto) {
        joinService.join(joinRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }
}
