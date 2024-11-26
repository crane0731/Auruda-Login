package com.sw.AurudaLogin.domain;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    // 현재 User 객체를 반환하는 메서드
    public User getUser() {
        return user;
    }

    //현재 user의 role을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                //앞에 "ROLE_" 접두사 필수
                return "ROLE_"+user.getRole().name();
            }
        });
        return collection;
    }

    //사용자의 role을 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //사용자의 id를 반환(이메일)
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }
}
