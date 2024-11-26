package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;

    //User와의 단방향 일대일 관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name="refresh_token",nullable = false)
    private String refreshToken;

    @Column(name="kakao_access_token")
    private String kakaoAccessToken;

    public RefreshToken(User user, String refreshToken,String kakaoAccessToken) {
        this.user= user;
        this.refreshToken = refreshToken;
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }

}
