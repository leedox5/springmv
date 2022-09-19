package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public MemberService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Member insertMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }
}
