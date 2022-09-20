package kr.leedox.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @Test
    public void getNameTest() {
        Member member = Member.builder().email("leedox@naver.com").build();
        assertEquals("leedox@naver.com", member.getEmail());
    }
}