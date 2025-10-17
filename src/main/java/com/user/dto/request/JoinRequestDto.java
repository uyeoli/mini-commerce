package com.user.dto.request;

import com.user.entity.Member;
import lombok.Getter;

@Getter
public class JoinRequestDto {
    private String loginId;
    private String password;
    private String name;
    private String email;

    public Member toEntity() {
        return Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .email(this.email)
                .build();
    }

}
