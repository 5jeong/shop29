package com.toy2.shop29.oauth2.dto;

import java.util.Map;

public class OAuth2ResponseFactory {

    public static OAuth2Response getOAuth2Response(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "naver":
                return new NaverResponse(attributes);
            case "google":
                return new GoogleResponse(attributes);
            case "kakao":
                return new KakaoResponse(attributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 로그인 제공자: " + registrationId);
        }
    }
}

