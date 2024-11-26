package com.sw.AurudaLogin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String role;
    private String Grade;
    private Long point;
    private String createdAt;
}
