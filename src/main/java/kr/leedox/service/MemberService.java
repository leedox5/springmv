package kr.leedox.service;

import kr.leedox.common.DataNotFoundException;
import kr.leedox.entity.Authority;
import kr.leedox.entity.Member;
import kr.leedox.entity.Role;
import kr.leedox.repository.MemberRepository;
import kr.leedox.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = false)
@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public MemberService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Member insertMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    public Member getMember(Integer id) {
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member was no found");
        }
    }

    public Member getMember(String name) {
        if("leedox@naver.com".equals(name)) {
            chkAdminUser(name);
        }
        Optional<Member> member = memberRepository.findByemail(name);
        if(member.isPresent()) {
            Member _member = member.get();
            _member.getAuthorities().stream().forEach(authority -> authority.getRole());
            return _member;
        } else {
            throw new DataNotFoundException("Member was not found");
        }
    }

    private void chkAdminUser(String name) {
        List<Role> roles = roleRepository.findByName("ROLE_ADMIN");
        Role role = Role.builder().name("ROLE_ADMIN").build();

        if(roles.isEmpty()) {
            role = roleRepository.save(role);
        }

        Optional<Member> _member = memberRepository.findByemail(name);

        Member member = Member.builder().email(name).username("이명호").build();
        member.setPassword("12345678");

        if(_member.isPresent()) {
            member = _member.get();
        } else {
            member = save(member);
        }

        if(member.getAuthorities().isEmpty()) {
            //Role role = roleRepository.findById(1).get();

            List<Authority> authorities = new ArrayList<>();
            Authority authority = Authority.builder().member(member).role(role).build();
            authorities.add(authority);
            member.setAuthorities(authorities);
            member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            memberRepository.save(member);
        }
    }

    public boolean isExistEmail(String email) {
		Optional<Member> member = memberRepository.findByemail(email);
        return member.isPresent();
	}

    public List<Member> getAllMember() {
        return memberRepository.findAll();
    }

    public Member save(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    public boolean chkPw(Integer id, Member member) {
        Member member1 = getMember(id);
        return passwordEncoder.matches(member.getPassword(), member1.getPassword());
    }

    public Member save(Integer id, String username, List<Role> roles) {
        Member member = getMember(id);

        member.setUsername(username);

        member.getAuthorities().clear();
        List<Authority> authorities = new ArrayList<>();
        for(Role role : roles) {
            authorities.add(Authority.builder().role(role).member(member).build());
        }
        member.setAuthorities(authorities);

        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return memberRepository.save(member);
    }

    public Member resetPassword(String email) {
        Member member = getMember(email);
        member.setPassword(passwordEncoder.encode("12345678"));
        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return memberRepository.save(member);
    }

    public Member resetPassword(Integer id) {
        Member member = getMember(id);
        member.setPassword(passwordEncoder.encode("12345678"));
        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return memberRepository.save(member);
    }
}
