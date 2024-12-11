package com.sw.AurudaLogin.dto;

import com.sw.AurudaLogin.domain.Grade;
import com.sw.AurudaLogin.domain.Role;
import com.sw.AurudaLogin.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequest {

    @NotBlank(message="이메일을 입력하세요.")
    private String email;

    @NotBlank(message="비밀번호를 입력하세요")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요")
    @Size(max = 10, message = "닉네임은 10자 이하로 입력하세요")
    private String nickname;

    private String profileImageUrl;


    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .profileImageUrl(this.profileImageUrl)
                .grade(Grade.E)
                .role(Role.USER)
                .point(0L)
                .build();
    }

}
