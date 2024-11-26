package com.sw.AurudaLogin.repository;


import com.sw.AurudaLogin.domain.Article;
import com.sw.AurudaLogin.domain.ArticleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    //userId로 게시글 찾기
    List<Article> findByUserId(Long userId);



}
