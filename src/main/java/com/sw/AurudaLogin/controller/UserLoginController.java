package com.sw.AurudaLogin.controller;

import com.sw.AurudaLogin.domain.CustomUserDetails;
import com.sw.AurudaLogin.domain.RefreshToken;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.dto.JoinRequest;
import com.sw.AurudaLogin.dto.LoginRequest;
import com.sw.AurudaLogin.dto.TokenResponse;
import com.sw.AurudaLogin.service.RefreshTokenService;
import com.sw.AurudaLogin.service.TokenService;
import com.sw.AurudaLogin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auruda/auth")
public class UserLoginController {

    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Valid @RequestBody JoinRequest request, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        // 1. 유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );


        }

        // 2. 이메일 중복 검사
        if (!userService.isEmailDuplicate(request.getEmail())) {
            errorMessages.put("email", "중복된 이메일입니다.");
        }


        // 4. 비밀번호 일치 확인
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            errorMessages.put("passwordCheck", "비밀번호가 일치하지 않습니다.");
        }

        // 5. 오류 메시지가 존재하면 이를 반환
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }

        // 6. 오류가 없다면 회원가입 처리

        userService.join(request);
        System.out.println("회원가입 성공");
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

            // 1. AuthenticationManager로 인증 처리
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. 인증 성공 시 유저 정보 가져오기
            CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();
            User user = customUserDetails.getUser(); // User 객체를 직접 가져옴

            // 3. 기존 Refresh Token이 있으면 삭제하여 로그아웃 처리
            refreshTokenService.findByUserId(user.getId())
                    .ifPresent(refreshTokenService::deleteByRefreshToken);

            // 4. Access Token 및 Refresh Token 생성
            TokenResponse tokenResponse = tokenService.createTokens(user,null);

            // 5. 토큰 응답 반환
            return ResponseEntity.ok(tokenResponse);


    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout( @RequestHeader("User-Id")Long userId) {



            User user = userService.findById(userId);

            RefreshToken userRefreshToken = refreshTokenService.findByUserId(user.getId()).orElse(null);


            // 3. 카카오 로그아웃 API 호출 (카카오 토큰이 있는 경우)
            if ((userRefreshToken.getKakaoAccessToken())!=null) {

                logoutFromKakao(userRefreshToken.getKakaoAccessToken());
            }


            // RefreshTokenService를 통해 리프레시 토큰을 삭제
            refreshTokenService.deleteByRefreshToken(userRefreshToken);

            return ResponseEntity.ok("로그아웃 성공");



  }// 카카오 로그아웃 API 호출 메서드
    private void logoutFromKakao(String accessToken) {
        String kakaoUnlinkUrl = "https://kapi.kakao.com/v1/user/unlink";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);  // Access Token 설정
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(kakaoUnlinkUrl, request, String.class);
            System.out.println("카카오 계정 연결 해제 성공: " + response.getBody());
        } catch (Exception e) {
            System.err.println("카카오 계정 연결 해제 실패: " + e.getMessage());
        }
    }


}

