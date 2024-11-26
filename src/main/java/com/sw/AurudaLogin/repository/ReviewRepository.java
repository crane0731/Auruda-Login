package com.sw.AurudaLogin.repository;

import com.sw.AurudaLogin.domain.Review;
import com.sw.AurudaLogin.domain.TravelPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    void deleteByUserId(Long userId);

}
