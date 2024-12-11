package com.sw.AurudaLogin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//로그인 시 데이터 받는 폼
@Getter @Setter
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
}
