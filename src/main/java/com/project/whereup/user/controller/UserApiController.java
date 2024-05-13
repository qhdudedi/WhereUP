package com.project.whereup.user.controller;

import com.project.whereup.user.dto.request.UserRequestDto;
import com.project.whereup.user.entity.User;
import com.project.whereup.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class UserApiController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> signup(@RequestBody UserRequestDto requestDto) {
        userService.save(requestDto.toEntity());
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable Long userId, @RequestBody UserRequestDto requestDto){
        User user = userService.update(userId, requestDto);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId){
        userService.deleteById(userId);
        return ResponseEntity.ok().build();
    }

}
