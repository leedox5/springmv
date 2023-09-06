package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WordServiceTest {
    @Autowired
    WordService wordService;

    @Autowired
    MemberService memberService;
    @Test
    void getListTest() {
        List<Wordbook> words = wordService.getList();
        assertEquals(7, words.size());
    }

    @Test
    void getWordBookTest() {
        Wordbook wordbook = wordService.getWordbook(1029);
        List<WordMeaning> wms = (List<WordMeaning>) wordbook.getWordMeanings();
        System.out.println(wms.size());
    }

    @Test
    void saveTest() {
        Member member = memberService.resetPassword("leedox@naver.com");
    }
}
