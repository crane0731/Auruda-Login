package com.sw.AurudaLogin.controller;
import com.sw.AurudaLogin.dto.CreateAccessTokenResponse;
import com.sw.AurudaLogin.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/auruda/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestHeader("Authorization")String authorizationHeader) {

        System.out.println("ddssss");
        String token = "";
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // "Bearer " 접두사 제거
            token = authorizationHeader.substring(7);
        }

        String newAccessToken = tokenService.createNewAccessToken(token);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }
}
