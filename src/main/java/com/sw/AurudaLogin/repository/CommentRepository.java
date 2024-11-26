package com.sw.AurudaLogin.repository;


import com.sw.AurudaLogin.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
