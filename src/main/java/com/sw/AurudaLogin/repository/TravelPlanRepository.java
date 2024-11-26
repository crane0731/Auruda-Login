package com.sw.AurudaLogin.repository;


import com.sw.AurudaLogin.domain.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    // 유저 아이디로 여행 계획 리스트 조회
    List<TravelPlan> findByUserId(Long userId);

}
