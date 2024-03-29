package kr.leedox.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.entity.Wordbook;
import kr.leedox.wordbook.WordCountDTO;
import kr.leedox.wordbook.WordCountInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.print.Pageable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureJson
class WordRepositoryTest {
    @Autowired
    WordRepository wordRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getWordTest() {
        Wordbook word = Wordbook.builder().word("cond").build();
        List<Wordbook> words = wordRepository.findByWordContaining(word.getWord());
        assertEquals(10, words.size());
        for (Wordbook w : words ) {
            System.out.println(w);
        }
        words = wordRepository.findByWordStartsWithOrderByWordAsc("cond");
        assertEquals(7, words.size());
    }

    @Test
    void findAllPagingTest() {
        List<Wordbook> words = wordRepository.findTop10ByOrderByUpdDateDesc();

        for(Wordbook wb : words) {
            System.out.println(wb.getUpdDate());
        }
    }

    @Test
    void findBySeqTest() {
        List<Wordbook> words = wordRepository.findTop10BySeqGreaterThanOrderBySeqAscIdAsc(-1);
        for(Wordbook wb : words) {
            System.out.println(wb.getWord() + wb.getSeq());
        }
    }

    @Test
    void findNextSeq() {
        List<Wordbook> words = wordRepository.findBySeqGreaterThanOrderBySeqAsc(-1);
        Boolean seqFound = false;

        for (int i = 1; i < 2000; i++) {
            seqFound = false;
            for(Wordbook word : words) {
                if(word.getSeq() == i) {
                    seqFound = true;
                    break;
                }
            }
            if(seqFound) {
                //System.out.println("Processing SEQ: " + i);
            } else {
                System.out.println("NEXT SEQ: " + i);
                break;
            }
        }
    }

    @Test
    void getWordCountTest() {
        List<WordCountDTO> wordCounts = wordRepository.findCount("3000");
        for(WordCountDTO dto : wordCounts) {
            System.out.println(dto.getUpdDate() + "==>" + dto.getCount());
        }
        assertEquals(5, wordCounts.size());
    }

    @Test
    void getWordCountNativeQueryTest() {
        List<WordCountInterface> wordCounts = wordRepository.findCountNativeQuery();
        for(WordCountInterface dto : wordCounts) {
            System.out.println(dto.getUpdDate() + "==>" + dto.getCount());
        }
        assertEquals(5, wordCounts.size());
    }
}