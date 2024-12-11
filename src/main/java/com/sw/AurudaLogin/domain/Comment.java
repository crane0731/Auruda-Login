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
@Table(name = "comment")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",nullable = false)
    private Article article;

    //부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    //자식 대댓글 리스트
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies=new ArrayList<>();

    @Column(name="content")
    private String content;

    @CreatedDate //엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    //부모 댓글을 설정하고 양방향 연관 관계를 맞춰주는 메서드
    public void addReply(Comment reply) {
        replies.add(reply);
        reply.setParent(this);
    }

    public void removeReply(Comment reply) {
        replies.remove(reply);
        reply.setParent(null);
    }

    public void deleteAllReplies() {
        if (replies != null) {
            for (Comment reply : new ArrayList<>(replies)) { // replies 리스트를 복사하여 순회
                removeReply(reply); // 개별적으로 removeReply 메서드 호출하여 관계를 정리하면서 삭제
            }
        }
    }

    public void update(String content){
        this.content = content;
    }

}
