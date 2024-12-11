package com.sw.AurudaLogin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String role;
    private String Grade;
    private Long point;
    private String createdAt;

}
