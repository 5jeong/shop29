package com.toy2.shop29.users.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class UserContext implements UserDetails, OAuth2User {

    private final UserDto userDto; // 회원 정보 (DB)
    private final List<GrantedAuthority> authorities; // 회원 권한(시큐리티에서 사용)
    private final Map<String, Object> attributes;  // 소셜 로그인에서 제공한 사용자 정보

    // 폼 로그인용 생성자
    public UserContext(UserDto userDto, List<GrantedAuthority> authorities) {
        this.userDto = userDto;
        this.authorities = authorities;
        this.attributes = null;  // 폼 로그인은 attributes가 없음
    }

    // 소셜 로그인용 생성자
    public UserContext(UserDto userDto, List<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.userDto = userDto;
        this.authorities = authorities;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return userDto.getUserId();
    }

    // OAuth2User 메서드 (소셜 로그시 모든 정보들)
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userDto.getUserName();
    }

}
