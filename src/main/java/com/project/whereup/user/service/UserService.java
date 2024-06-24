package com.project.whereup.user.service;

import com.project.whereup.oauth.PrincipalDetails;
import com.project.whereup.user.dto.request.UserRequestDto;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailService userDetailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //가입 signup
    @Transactional
    public Long save(UserRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new IllegalArgumentException("동일한 이메일이 존재합니다.");
        }
        return userRepository.save(requestDto.toEntity(bCryptPasswordEncoder)).getId();
    }
    //소셜 회원가입
    @Transactional
    public void saved(User user){
        userRepository.save(user);
    }

    // 수정 update
    @Transactional
    public User update(Long id, UserRequestDto userRequestDto){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found memberInfo"));
        user.update(userRequestDto.getName(), userRequestDto.getEmail(),userRequestDto.getPassword(), userRequestDto.getBirth(), userRequestDto.getNickname());
        return user;
    }
    // 삭제
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
    @Transactional
    public boolean checkNicknameDuplicate(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    // 마이페이지 정보 조회
    @Transactional
    public User getMyPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        log.info("Principal class: {}", principal.getClass().getName());

        if (principal instanceof PrincipalDetails) {
            PrincipalDetails userDetails = (PrincipalDetails) principal;
            log.info("Authenticated user: {}", userDetails.getUsername());
            return userDetails.getUser();
        } else if (principal instanceof String) {
            try {
                PrincipalDetails userDetails = (PrincipalDetails) userDetailService.loadUserByUsername((String) principal);
                log.info("Authenticated user: {}", userDetails.getUsername());
                return userDetails.getUser();
            } catch (UsernameNotFoundException e) {
                log.error("User not found: {}", principal);
                throw new IllegalStateException("User not found: " + principal, e);
            }
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass().getName());
        }
    }

    // 마이페이지 정보 수정
    @Transactional
    public User updateMyPage(UserRequestDto userRequestDto) {
        User loggedInUser = getMyPage();
        User updatedUser = update(loggedInUser.getId(), userRequestDto);
        updateAuthentication(updatedUser); // 수정된 사용자 정보로 인증 정보 갱신
        log.info("update success");
        return updatedUser;
    }
    // 사용자 세션 인증 정보 업데이트
    private void updateAuthentication(User user) {
        PrincipalDetails userDetails = new PrincipalDetails(user);
        log.info("Updating authentication for user: {}", userDetails);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        log.info("New authentication: {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
