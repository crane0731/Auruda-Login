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

    //카카오 콜백-인증 코드 수신 및 사용자 정보 처리
    @GetMapping("/kakao/callback")
    public ResponseEntity<Object> KakaoCallback(@RequestParam String code){

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        String accessToken = kakaoService.getKakaoAccessToken(code);
        KakaoUserDetail userDetail = kakaoService.getKakaoUserDetail(accessToken);

        //JWT 토큰을 생성하여 응답으로 제공하거나, 사용자 정보를 그대로 반환할 수 있습니다.
        JoinRequest request = JoinRequest.builder()
                .email(userDetail.getEmail())
                .nickname(userDetail.getNickname())
                .profileImageUrl(userDetail.getProfileImageUrl())
                .password("0000")
                .passwordCheck("0000")
                .build();

        User user;

        try {
            // 기존 회원을 조회
            user = userService.findByEmail(request.getEmail());
        } catch (CustomException e) { // 사용자 정의 예외를 사용한 경우, 예외 이름에 맞게 변경
            // 예외 발생 시, 회원 가입 진행

            // 이메일 중복 검사
            if (!userService.isEmailDuplicate(request.getEmail())) {
                errorMessages.put("email", "중복된 이메일입니다.");
            }

            // 오류 메시지가 존재하면 이를 반환
            if (!errorMessages.isEmpty()) {
                return ResponseEntity.badRequest().body(errorMessages);
            }
//            request.setKakaoAccessToken(accessToken);
            userService.join(request);

            // 가입 후 새로 가입된 사용자 정보 다시 가져오기
            user = userService.findByEmail(request.getEmail());
        }

        // SecurityContext에 인증 객체 생성 및 설정
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 기존 Refresh Token이 있으면 삭제하여 로그아웃 처리
        refreshTokenService.findByUserId(user.getId())
                .ifPresent(refreshTokenService::deleteByRefreshToken);

        // 3. Access Token 및 Refresh Token 생성
        TokenResponse tokenResponse = tokenService.createTokens(user,accessToken);

        // 토큰 응답 반환
        return ResponseEntity.ok(tokenResponse);

    }



}
