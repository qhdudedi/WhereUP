package com.project.whereup.member.service;

import com.project.whereup.member.dto.request.MemberRequestDto;
import com.project.whereup.member.entity.Member;
import com.project.whereup.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 가입-등록
    public Member save(Member member) {
       return memberRepository.save(member);
    }

    // 수정 upate
    @Transactional
    public Member update(Long id,MemberRequestDto memberRequestDto){
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found memberInfo"));
        member.update(memberRequestDto.getName(), memberRequestDto.getEmail(), memberRequestDto.getPassword(), memberRequestDto.getBirth(),memberRequestDto.getNickname());
        return member;
    }
    // 삭제
    public void deleteById(Long id){
        memberRepository.deleteById(id);
    }
}
