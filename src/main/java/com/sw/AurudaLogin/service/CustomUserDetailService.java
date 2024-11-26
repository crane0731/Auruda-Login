package com.sw.AurudaLogin.service;

import com.sw.AurudaLogin.domain.CustomUserDetails;
import com.sw.AurudaLogin.domain.User;
import com.sw.AurudaLogin.exception.CustomException;
import com.sw.AurudaLogin.exception.ErrorCode;
import com.sw.AurudaLogin.exception.ErrorMessage;
import com.sw.AurudaLogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    //사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_USER));
        return new CustomUserDetails(user);
    }
}
