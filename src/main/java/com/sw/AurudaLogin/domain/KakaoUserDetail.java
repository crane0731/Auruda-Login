package com.sw.AurudaLogin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserDetail {

    private Long id; //카카오 고유 사용자 ID
    private String nickname; //사용자 닉네임
    private String profileImageUrl;//프로필 이미지 URL
    private String email; // 이메일

    @Override
    public String toString() {
        return "KakaoUserDetail{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
