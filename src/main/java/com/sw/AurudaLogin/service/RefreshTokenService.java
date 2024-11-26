package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.domain.RefreshToken;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.exception.ErrorCode;
import com.sw.AurudaLogin.exception.ErrorMessage;
import com.sw.AurudaLogin.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;


    public Optional<RefreshToken> findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    //전달 받은 리프레시 토큰으로 리프레시 토큰 객체를 검색해서 전달하는 메서드
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new CustomException(ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_REFRESH_TOKEN));
    }

    // 리프레시 토큰을 찾아 삭제하는 메서드
    public void deleteByRefreshToken(RefreshToken refreshToken) {
        // 데이터베이스에서 리프레시 토큰 삭제
        refreshTokenRepository.delete(refreshToken);
    }
}
