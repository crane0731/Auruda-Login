package com.sw.AurudaLogin.config;

import com.sw.AurudaLogin.config.jwt.TokenProvider;
import com.sw.AurudaLogin.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;  // TokenProvider 주입
    private final CustomUserDetailService customUserDetailService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }



    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TokenAuthenticationFilter 생성 및 Security 필터 체인에 추가
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenProvider);

        http
                .cors(cors -> cors.disable())  // CORS 비활성화 또는 필요 시 설정 가능
                //.cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 활성화
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless 모드
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auruda/auth/signup", "/api/auruda/auth/login", "/api/auth/kakao", "/api/auth/kakao/callback").permitAll()  // 인증 없이 접근 가능
                        .requestMatchers("/api/auruda/users/admin/**").hasRole("ADMIN")  // 관리자만 접근 가능
                        .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
                )
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 필터 추가


        return http.build();
    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://192.168.56.1:3000", "http://localhost:8000", "http://192.168.56.1:8000")); // 허용할 도메인 설정
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(List.of("*"));
//        configuration.setAllowCredentials(true); // 인증 정보 허용
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    // CORS 필터를 Bean으로 등록
        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://192.168.56.1:3000", "http://localhost:8000", "http://192.168.56.1:8000")); // 허용할 도메인 설정
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(true); // 인증 정보 허용

            source.registerCorsConfiguration("/**", configuration);
            return new CorsFilter(source);
        }

    // 패스워드 인코더로 사용할 Bean 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

