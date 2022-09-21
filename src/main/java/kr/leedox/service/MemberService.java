package kr.leedox.service;

import kr.leedox.common.DataNotFoundException;
import kr.leedox.entity.Member;
import kr.leedox.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Member getMember(String name) {
        Optional<Member> member = memberRepository.findByemail(name);
        if(member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member was not found");
        }
    }

	public boolean isExistEmail(String email) {
		Optional<Member> member = memberRepository.findByemail(email);
        return member.isPresent();
	}
}
