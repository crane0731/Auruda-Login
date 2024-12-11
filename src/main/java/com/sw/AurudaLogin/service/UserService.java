package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.domain.Article;
import com.sw.AurudaLogin.domain.TravelPlan;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.dto.JoinRequest;
import com.sw.AurudaLogin.dto.PassworUpdateRequestDto;
import com.sw.AurudaLogin.dto.UpdateUserRequest;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.exception.ErrorCode;
import com.sw.AurudaLogin.exception.ErrorMessage;
import com.sw.AurudaLogin.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ArticleRepository articleRepository;
    private final ReviewRepository reviewRepository;
    private final StorageRespository storageRespository;
    private final TravelPlanRepository travelPlanRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(JoinRequest dto){
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        userRepository.save(dto.toEntity());
    }


    // 이메일 중복 검사
    public boolean isEmailDuplicate(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            return false;
        }
        return true;
    }

    @Transactional//트랜잭션 메서드
    public void update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
//        String password = bCryptPasswordEncoder.encode(request.getPassword());
        user.update(request.getNickname(), request.getProfileImageUrl());


    }

    //비밀번호 변경 서비스
    @Transactional//트랜잭션 메서드
    public void updatePassword(Long id, PassworUpdateRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() ->new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
        String password = bCryptPasswordEncoder.encode(dto.getPassword());
        user.updatePassword(password);
    }

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
    }


    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
    }

    //모든 회원을 조회
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {

        //게시글 삭제
        articleRepository.findByUserId(userId)
                .forEach(article -> {
                    article.deleteAllComments();
                    articleRepository.delete(article);
                });

        //리뷰 삭제
        reviewRepository.deleteByUserId(userId);

        //여행 계획 삭제
        travelPlanRepository.findByUserId(userId)
                .forEach(travelPlan -> {
                    travelPlan.deleteAllTravelPlanTravelPlace(); // 연관 데이터 삭제
                    travelPlanRepository.delete(travelPlan); // TravelPlan 삭제
                });

        //저장소 삭제
        storageRespository.findByUserId(userId)
                .forEach(storage -> {
                    storage.deleteAllStorageTravelPlace();
                    storageRespository.delete(storage);
                });

        //리프레쉬 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);
        //유저 삭제
        userRepository.deleteById(userId);
    }


}