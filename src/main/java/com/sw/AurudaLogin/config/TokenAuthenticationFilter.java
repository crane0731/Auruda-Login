package com.sw.AurudaLogin.config;

import com.sw.AurudaLogin.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);


        //가져온 값에서 접두사 제거
        String token = getAcessToken(authorizationHeader);

        //가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
        if(tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else {
            System.out.println("인증 정보가 맞지 않음");
        }
        filterChain.doFilter(request, response);

    }

    private String getAcessToken(String authorizationHeader) {
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}

//요청 헤더에서 키가 'Authorization'인 필드의 값을 가져온 다음 토큰의 접두사 Bearer를 제외 한 값을 얻습니다. 만약 값이 null 이거나 Bearer로 시작하지 않으면 null을 반환합니다.
// 이어서 가져온 토큰이 유효한지 확인하고, 유효하다면 인증 정보를 관리하는 시큐리티 컨텍스트에 인증 정보를 설정합니다. 위에서 작성한 코드가 실행되며 인증 정보가 설정된 이후에 컨텍스트 홀더에서
//getAuthentication() 메서드를 사용해 인증 정보를 가져오면 유저 객체가 반환됩니다. 유저 객체에는 유저이름과 권한 목록과 같은 인증 정보가 포함됩니다.
