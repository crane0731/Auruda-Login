package com.sw.AurudaLogin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateUserRequest {

    @NotBlank(message = "닉네임을 입력하세요")
    @Size(max = 10, message = "닉네임은 10자 이하로 입력하세요")
    private String nickname;

    private String profileImageUrl;

}