package com.sw.AurudaLogin.dto;

import com.sw.AurudaLogin.domain.User;
import lombok.Getter;


@Getter
public class FriendListResponse {

    private String email;
    private String nickname;
    private String grade;

    public FriendListResponse(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.grade = user.getGrade().getCommentary();
    }
}
