package com.sw.AurudaLogin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserFriendRequest {
    private String userEmail;
    private String friendEmail;
}
