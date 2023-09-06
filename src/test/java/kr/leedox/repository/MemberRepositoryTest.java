package kr.leedox.repository;

import kr.leedox.entity.Authority;
import kr.leedox.entity.Member;
import kr.leedox.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void saveMemberTest() {
        // given
        String email = "leedox@leedox.kr";
        Member member = Member.builder().email(email).build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertEquals(email, savedMember.getEmail());
    }

    @Test
    void updateMemberTest() {
        Member member = memberRepository.findByemail("leedox@naver.com").get();
        Role role = roleRepository.findById(1).get();

        List<Authority> authorities = new ArrayList<>();
        Authority authority = Authority.builder().member(member).role(role).build();
        authorities.add(authority);
        member.setAuthorities(authorities);
        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        memberRepository.save(member);

        assertEquals(1, member.getAuthorities().size());
    }

    @Test
    void saveTest() {
        Member member = memberRepository.findByemail("leedoox@naver.com").get();

        member.setPassword("12345678");

    }

    @Test
    void roleTest() {
        Role role = Role.builder().name("ROLE_GOLD").build();
        roleRepository.save(role);

        assertEquals("ROLE_GOLD", role.getName());
    }
}