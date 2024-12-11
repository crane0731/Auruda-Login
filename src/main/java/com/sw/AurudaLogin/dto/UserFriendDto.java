package com.sw.AurudaLogin.dto;

import com.sw.AurudaLogin.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFriendDto {
    private User user;
    private User friend;
}