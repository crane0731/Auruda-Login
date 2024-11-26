package com.sw.AurudaLogin.dto;

import com.sw.AurudaLogin.domain.User;
import lombok.Getter;

@Getter
public class AdminUserListResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String grade;

    public AdminUserListResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.grade = user.getGrade().getCommentary();
    }
}
