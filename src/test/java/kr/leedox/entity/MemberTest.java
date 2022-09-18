package kr.leedox.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @Test
    public void getNameTest() {
        Member member = Member.builder().userId("leedox").build();
        assertEquals("leedox", member.getUserId());
    }
}