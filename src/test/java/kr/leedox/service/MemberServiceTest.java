package kr.leedox.service;

import kr.leedox.entity.Authority;
import kr.leedox.entity.Member;
import kr.leedox.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName(("MemberService Test"))
public class MemberServiceTest {
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    @Test
    @DisplayName("insert() test")
    void insertTest() {
        // given
        Member member = Member.builder().email("leedox@naver.com").build();
        ReflectionTestUtils.setField(member, "id", 1);
        member.setPassword("1234");

        Member member1 = Member.builder().email("leedox@naver.com").build();
        member1.setPassword(passwordEncoder.encode("1234"));

        given(memberRepository.save(any())).willReturn(member1);

        // when
        Member savedMember = memberService.insertMember(member);

        // then
        assertEquals("leedox@naver.com", savedMember.getEmail());
        assertTrue(passwordEncoder.matches("1234", savedMember.getPassword()));
    }
    @Test
    @DisplayName("get() Test")
    void selectTest() {

        given(memberRepository.findByemail(any())).willReturn(Optional.ofNullable(getStubMember()));

        Member member = memberService.getMember("leedox@naver.com");

        assertEquals("leedox@naver.com", member.getEmail());
        assertEquals(1, member.getAuthorities().size());
    }

    private Member getStubMember() {
        Member member = Member.builder().email("leedox@naver.com").build();
        return member;
    }
}
