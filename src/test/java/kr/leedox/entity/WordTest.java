package kr.leedox.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordTest {

    @Test
    void getWordTest() {
        Wordbook word = Wordbook.builder().word("reveal").build();
        assertEquals("reveal", word.getWord());
    }
}
