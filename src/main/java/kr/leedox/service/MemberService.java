package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member insertMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }
}
