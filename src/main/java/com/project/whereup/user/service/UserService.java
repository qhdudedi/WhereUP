package com.project.whereup.user.service;

import com.project.whereup.user.dto.request.UserRequestDto;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        user.update(userRequestDto.getName(), userRequestDto.getEmail(), bCryptPasswordEncoder.encode(userRequestDto.getPassword()), userRequestDto.getBirth(), userRequestDto.getNickname());
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

}
