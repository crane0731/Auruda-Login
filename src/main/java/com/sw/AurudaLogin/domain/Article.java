package com.sw.AurudaLogin.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "article")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name = "recommendation",nullable = false)
    private Long recommendation;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "articleType",nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleType articleType;

    @Column(name = "travel_plan_id")
    private Long travelPlanId;

    public void addRecommendation(){
        recommendation++;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setArticle(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setArticle(null);
    }

    public void deleteAllComments() {
        if (comments != null) {
            for (Comment comment : new ArrayList<>(comments)) { // comments 리스트를 복사하여 순회
                removeComment(comment); // 각 댓글을 관계 정리하면서 삭제
            }
        }
    }

    public void update(String title, String content, String articleType){
        this.title = title;
        this.content = content;
        this.articleType = ArticleType.valueOf(articleType);

    }



}
