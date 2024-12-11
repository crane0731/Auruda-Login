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
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_place", nullable = false)
    private TravelPlace travelPlace;

    @Column(name = "content")
    private String content;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    public void update(String content, Double rating) {
        this.content = content;
        this.rating = rating;
    }
}
