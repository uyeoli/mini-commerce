package com.user.controller;

import com.user.dto.request.MemberUpdateDto;
import com.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id,
                                             @RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.updateMember(id, memberUpdateDto);
        return ResponseEntity.ok().build();
    }
}
