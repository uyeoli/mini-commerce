package com.user.dto.request;

import com.user.entity.Member;
import lombok.Getter;

@Getter
public class JoinRequestDto {
    private String loginId;
    private String password;
    private String name;
    private String email;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .name(this.name)
                .email(this.email)
                .build();
    }

}
