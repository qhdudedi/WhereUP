package com.project.whereup.oauth;

import com.project.whereup.user.entity.User;
import com.project.whereup.user.entity.eum.Role;
import com.project.whereup.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        OAuth2Response oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider");
        }

//        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
//            System.out.println("네이버 로그인 요청");
//            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
//        }
//        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
//            Map<String, Object> attributes = oAuth2User.getAttributes();
//            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//
//            // KakaoUserInfo 객체를 생성합니다
//            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(kakaoAccount);
//            oAuth2UserInfo = new
//        }else {
//            System.out.println("구글과 페이스북, 네이버 로그인만 가능합니다.");
//        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;

//        User userEntity = userRepository.findByUsername(username);
        User userEntity = userRepository.findByEmail(email);
        System.out.println(userEntity);
        if(userEntity == null) {
            System.out.println("Oauth인이 최초입니다.");
            userEntity = User.builder()
                    .name(name)
                    .email(email)
                    .nickname(name)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(userEntity);
        } else {
            System.out.println("로그인을 이미 한 적이 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
