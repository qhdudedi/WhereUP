package com.project.whereup.oauth.kakao;

import com.project.whereup.oauth.kakao.dto.KakaoUserInfo;
import com.project.whereup.user.dto.CustomUserDetails;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.entity.eum.Role;
import com.project.whereup.user.repository.UserRepository;
import com.project.whereup.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final KakaoOAuth2 kakaoOAuth2;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void kakaoLogin(String code) {
        log.info("kakaoLogin Service 호출");
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(code);

        log.info("userInfo.getNickname() = " + userInfo.getNickname());

        // 카카오에서 받아온 사용자의 정보
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        //이미 존재하는 회원인지, email을 통해 확인
        User kakaoMember = userRepository.findByEmail(email);

        //존재하지 않는 회원이라면, 회원가입 진행
        if (kakaoMember == null) {
            User sameEmailMember = userRepository.findByEmail(email);
            if(sameEmailMember== null){         //새로운 kakaoUser객체 만들어서 DB에 저장(회원가입)
                String userName = nickname;
                String password = " ";
                String encodedPassword = bCryptPasswordEncoder.encode(password);
                kakaoMember = User.builder()
                        .email(email)
                        .nickname(nickname)
                        .name(userName)
                        .email(email)
                        .password(encodedPassword)
                        .role(Role.USER)
                        .kakaoId(kakaoId.toString())
                        .build();
                userService.saved(kakaoMember);
            }
        }
        // kakaoMember 객체에 로그인 할 정보를 담고있음 - 로그인 처리
        // 스프링 시큐리티 통해 인증된 사용자로 등록
        CustomUserDetails userDetails = new CustomUserDetails(kakaoMember);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("카카오 로그인 성공: " + kakaoMember.getEmail());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("auth.getName() = " + auth.getName());
    }
}
