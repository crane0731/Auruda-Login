package com.sw.AurudaLogin.controller;

import com.sw.AurudaLogin.domain.KakaoUserDetail;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.dto.JoinRequest;
import com.sw.AurudaLogin.dto.TokenResponse;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.service.KakaoService;
import com.sw.AurudaLogin.service.RefreshTokenService;
import com.sw.AurudaLogin.service.TokenService;
import com.sw.AurudaLogin.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final UserService userService;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.api.client-id}")
    private String clientId;

    //카카오 로그인 시작- 카카오 인증 페이지로 리다이렉트
    @GetMapping("/kakao")
    public void KakaoLogin(HttpServletResponse response)throws IOException {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id="+clientId +
                "&redirect_uri=http://192.168.56.1:8081/api/auth/kakao/callback" +
                "&response_type=code";

        response.sendRedirect(kakaoAuthUrl);
    }

    // 카카오 인증 콜백 처리
    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 카카오에서 Access Token 받기
        String kakaoAccessToken = kakaoService.getKakaoAccessToken(code);
        KakaoUserDetail userDetail = kakaoService.getKakaoUserDetail(kakaoAccessToken);

        // 회원 가입 또는 기존 회원 정보 조회
        JoinRequest request = JoinRequest.builder()
                .email(userDetail.getEmail())
                .nickname(userDetail.getNickname())
                .profileImageUrl(userDetail.getProfileImageUrl())
                .password("0000")
                .passwordCheck("0000")
                .build();

        User user;

        try {
            user = userService.findByEmail(request.getEmail());
        } catch (Exception e) {
            userService.join(request);
            user = userService.findByEmail(request.getEmail());

        }

        // SecurityContext에 인증 객체 생성 및 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 기존 Refresh Token 삭제
        refreshTokenService.findByUserId(user.getId())
                .ifPresent(refreshTokenService::deleteByRefreshToken);

        // Access Token 및 Refresh Token 생성
        TokenResponse tokenResponse = tokenService.createTokens(user, kakaoAccessToken);

        // 프론트엔드 메인 페이지로 리다이렉트, 토큰 포함
        String redirectUrl = "http://localhost:3000/home?accessToken=" + tokenResponse.getAccessToken()
                + "&refreshToken=" + tokenResponse.getRefreshToken();
        response.sendRedirect(redirectUrl);
    }




}