package com.project.whereup.oauth.kakao;

import com.project.whereup.oauth.kakao.dto.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoOAuth2 {
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    public KakaoUserInfo getUserInfo(String authorizedCode) {
        log.info("getUserInfo 호출");
        // 인가코드 -> 액세스 토큰
        String accessToken = getAccessToken(authorizedCode);
        // 액세스 토큰 -> 카카오 사용자 정보
        KakaoUserInfo userInfo = getUserInfoByToken(accessToken);

        return userInfo;
    }

    private String getAccessToken(String authorizedCode) {
        log.info("getAccessToken 호출");
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);    //REST_API
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("client_secret",KAKAO_CLIENT_SECRET);
        params.add("code", authorizedCode);

        // HttpHeader와 HttpBody를 하나로 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // POST 방식으로, Http 요청하기, response 변수의 응답 받음
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON -> 액세스 토큰 파싱
        String tokenJson = response.getBody();
        JSONObject rjson = new JSONObject(tokenJson);
        String accessToken = rjson.getString("access_token");
        String refreshToken = rjson.getString("refresh_token");
        log.info("accessToken = " + accessToken);
        log.info("refreshToken = " + refreshToken);
        return accessToken;
    }
    //토큰을 통해 사용자 정보 가져오기
    KakaoUserInfo getUserInfoByToken(String accessToken) {
        // HttpHeader  생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나로 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        JSONObject body = new JSONObject(response.getBody());
        Long id = body.getLong("id");
        String email = body.getJSONObject("kakao_account").getString("email");
        String nickname = body.getJSONObject("properties").getString("nickname");

        //가져온 사용자 정보를 객체로 만들어서 반환
        return new KakaoUserInfo(id, email, nickname);
    }


    }
