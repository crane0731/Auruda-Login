package com.sw.AurudaLogin.config.jwt;

import com.sw.AurudaLogin.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

//토큰을 생성하고 올바른 토큰인지 유효성 검사를 하고 토큰에서 필요한 정보를 가져오는 클래스
@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime()+expiredAt.toMillis()),user);

    }

    //JWT 토큰 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE) //헤더 typ :  JWT
                //내용 iss : dlwnsgkr8318@skuniv.ac.kr(properties 파일에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)//내용 iat: 현재 시간
                .setExpiration(expiry) //내용 exp : expiry 멤버 변숫값
                .setSubject(user.getEmail())//내용 sub : 유저의 이메일
                .claim("id",user.getId())//클레임 id: 유저 id
                //서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .claim("role",user.getRole().name())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

    }

    //JWT 토큰 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀 값으로 복호화
                    .parseClaimsJws(token);
            return true;
        }catch(Exception e) {//복호화 과정에서 에러나 나면 유효하지 않은 토큰
            return false;
        }
    }


    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        // 1. 토큰에서 클레임 정보 추출
        Claims claims = getClaims(token);

        // 2. 클레임에서 역할(ROLE) 정보 추출 (예: "ROLE_USER" 또는 "ROLE_ADMIN")
        String role = claims.get("role", String.class);  // JWT 클레임에 저장된 권한 정보

        // 3. 권한 리스트 생성
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role));

        // 4. UsernamePasswordAuthenticationToken을 생성하여 인증 정보 반환
        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), // 사용자 정보 설정
                token,  // 토큰을 자격 증명으로 사용
                authorities  // 동적으로 설정된 권한
        );
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()//클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

    }


}
