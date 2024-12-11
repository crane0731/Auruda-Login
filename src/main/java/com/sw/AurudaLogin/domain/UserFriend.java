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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserFriend {

    @EmbeddedId
    private UserFriendId id; // 복합 키 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")  // 복합 키의 userId 필드와 매핑
    @JoinColumn(name = "user_id", nullable = false) // user_id 컬럼
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("friendId")  // 복합 키의 friendId 필드와 매핑
    @JoinColumn(name = "friend_id", nullable = false) // friend_id 컬럼
    private User friend;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public void updateUserFriendStatus(Status status) {
        this.status = status;
    }



}
