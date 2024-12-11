package com.sw.AurudaLogin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PassworUpdateRequestDto {

    @NotBlank(message="비밀번호를 입력하세요")
    private String password;
    private String passwordCheck;
}