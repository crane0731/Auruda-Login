package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.config.jwt.TokenProvider;
import com.sw.AurudaLogin.domain.RefreshToken;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.dto.TokenResponse;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.exception.ErrorCode;
import com.sw.AurudaLogin.exception.ErrorMessage;
import com.sw.AurudaLogin.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;


//전달 받은 리프레시 토큰으로 토큰 유효성 검사를 진행하고 유효한 토큰일 때 리프레시 토큰으로 사용자 ID를 찾는다.
// 사용자 ID로 사용자를 찾은 후에 토큰 제공자의 generateToken()을 메서드를 호출해서 새로운 엑세스 토큰을 생성한다.
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    private final Duration accessTokenValidity = Duration.ofHours(1);  // Access Token 유효 시간 (1시간)
    private final Duration refreshTokenValidity = Duration.ofDays(1);  // Refresh Token 유효 시간 (1일)
    //리프레쉬 토큰을사용해 엑세스 토큰 재발급
    public String createNewAccessToken(String refreshToken){
        //토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validateToken(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REQUEST, ErrorMessage.INVALID_REFRESH_TOKEN);
        }

        User user = refreshTokenService.findByRefreshToken(refreshToken).getUser();

        return tokenProvider.generateToken(user, accessTokenValidity);
    }

    //처음 로그인할때 엑세스 토큰,리프레쉬 토큰을 생성해주는 메서드
    public TokenResponse createTokens(User user,String kakaoAccessToken){

        //엑세스 토큰 생성
        String accessToken = tokenProvider.generateToken(user, accessTokenValidity);

        //리프레쉬 토큰 생성
        String refreshToken = tokenProvider.generateToken(user, refreshTokenValidity);

        //리프레쉬 토큰을 DB에 저장
        refreshTokenRepository.save(new RefreshToken(user, refreshToken,kakaoAccessToken));

        //엑세스 토큰 및  리프레쉬 토큰 반환
        return new TokenResponse(accessToken, refreshToken);

    }

}
