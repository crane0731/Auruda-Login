package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.domain.KakaoUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


//카카오 API와 통신해 Access Token을 요청하고 사용자 정보를 받아오는 서비스

@Service
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.api.client-id}")
    private String clientId;

    @Value("${kakao.api.client-secret}")
    private String clientSecret;

    public String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret); // 실제 비밀 키 입력
        params.add("redirect_uri", "http://192.168.56.1:8081/api/auth/kakao/callback");

        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            // 디버깅용 로그
            System.out.println("Request URL: " + tokenUrl);
            System.out.println("Request Headers: " + headers);
            System.out.println("Request Parameters: " + params);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

            // 응답 확인
            System.out.println("Token response: " + response.getBody());

            return (String) response.getBody().get("access_token");
        } catch (HttpClientErrorException e) {
            // 구체적인 오류 로그 출력
            System.out.println("Error during token request: " + e.getMessage());
            System.out.println("Response Body: " + e.getResponseBodyAsString());
            System.out.println("Status Code: " + e.getStatusCode());
            throw e;
        }
    }
    public KakaoUserDetail getKakaoUserDetail(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        Map<String, Object> userInfo = response.getBody();

        Long id = ((Number) userInfo.get("id")).longValue();
        Map<String, Object> properties = (Map<String, Object>) userInfo.get("properties");
        String nickname = (String) properties.get("nickname");
        String profileImageUrl = (String) properties.get("profile_image");

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        return new KakaoUserDetail(id, nickname, profileImageUrl, email);
    }
}




