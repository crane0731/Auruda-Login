package com.sw.AurudaLogin.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name="profileimage_url")
    private String profileImageUrl;

    @Column(name="point", nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade",nullable = false)
    private Grade grade;

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    public void update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}