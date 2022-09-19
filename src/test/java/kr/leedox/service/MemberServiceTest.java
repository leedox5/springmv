package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName(("MemberService Test"))
public class MemberServiceTest {
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    @Test
    @DisplayName("insert() test")
    void insertTest() {
        // given
        Member member = Member.builder().email("leedox@naver.com").password("1234").build();
        given(memberRepository.save(member)).willReturn(Member.builder().email("leedox@naver.com").password(passwordEncoder.encode("1234")).build());

        // when
        Member savedMember = memberService.insertMember(member);

        // then
        assertEquals("leedox@naver.com", savedMember.getEmail());
        assertTrue(passwordEncoder.matches("1234", savedMember.getPassword()));
    }
}
