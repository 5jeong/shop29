package com.toy2.shop29.oauth2.service;

import com.toy2.shop29.oauth2.dto.GoogleResponse;
import com.toy2.shop29.oauth2.dto.KakaoResponse;
import com.toy2.shop29.oauth2.dto.NaverResponse;
import com.toy2.shop29.oauth2.dto.OAuth2Response;
import com.toy2.shop29.users.domain.UserContext;
import com.toy2.shop29.users.domain.UserDto;
import com.toy2.shop29.users.service.user.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes = " + attributes);

        // 구글인지 네이버인지 카카오인지
        String registration = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registration.equals("naver")) {
            log.info("naver 로그인");
            oAuth2Response = new NaverResponse(attributes);
        } else if (registration.equals("google")) {
            log.info("google 로그인");
            oAuth2Response = new GoogleResponse(attributes);
        } else if (registration.equals("kakao")) {
            log.info("kakao 로그인");
            oAuth2Response = new KakaoResponse(attributes);
        } else {
            throw new IllegalArgumentException("지원하지 않는 로그인 제공자");
        }

        UserDto user = getUserInfo(oAuth2Response);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole()));
        return new UserContext(user, authorities, attributes);
    }

    private UserDto getUserInfo(OAuth2Response oAuth2Response) {
        UserDto user = userService.findById(oAuth2Response.getProviderId());

        // 만약 DB에 존재하지 않으면 새로운 사용자 저장
        if (user == null) {
            UserDto newUser = new UserDto();
            newUser.setUserId(oAuth2Response.getProviderId());  // 소셜 로그인 제공자의 고유 ID 사용
            newUser.setUserName(oAuth2Response.getName());      // 소셜 로그인에서 제공된 이름
            newUser.setEmail(oAuth2Response.getEmail());        // 소셜 로그인에서 제공된 이메일
            newUser.setJoinType(oAuth2Response.getProvider());  // 소셜 로그인 제공자 정보 (google, naver, kakao 등)
            newUser.setProviderId(oAuth2Response.getProviderId());
            newUser.setPassword(UUID.randomUUID().toString().substring(0, 10));  // 비밀번호는 더미 값(소셜로그인에서는 필요없음)
            userService.saveSocialUser(newUser);
            return newUser;
        }
        return user;
    }
}

