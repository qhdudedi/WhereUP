package com.project.whereup.user.service;

import com.project.whereup.user.dto.request.UserRequestDto;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 가입-등록
    public User save(User user) {
       return userRepository.save(user);
    }

    // 수정 upate
    @Transactional
    public User update(Long id, UserRequestDto userRequestDto){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found memberInfo"));
        user.update(userRequestDto.getName(), userRequestDto.getEmail(), userRequestDto.getPassword(), userRequestDto.getBirth(), userRequestDto.getNickname());
        return user;
    }
    // 삭제
    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
}
