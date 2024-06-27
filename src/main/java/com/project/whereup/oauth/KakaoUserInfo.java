package com.project.whereup.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2Response{
    private Map<String, Object> attributes; //getAttributes()

    public KakaoUserInfo(Map<String, Object> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("Attributes map cannot be null");
        }
        this.attributes = attributes;
    }
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        return (String) kakaoAccount.get("email");
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null) {
            return String.valueOf(properties.get("nickname"));
        }
        return null;
    }
}
