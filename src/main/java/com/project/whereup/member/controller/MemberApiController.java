package com.project.whereup.member.controller;

import com.project.whereup.member.dto.request.MemberRequestDto;
import com.project.whereup.member.entity.Member;
import com.project.whereup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto requestDto) {
        memberService.save(requestDto.toEntity());
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable Long userId, @RequestBody MemberRequestDto requestDto){
        Member member = memberService.update(userId, requestDto);
        return ResponseEntity.ok().body(member);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId){
        memberService.deleteById(userId);
        return ResponseEntity.ok().build();
    }

}
