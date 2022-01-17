package kr.leedox.service;

import kr.leedox.entity.Wordbook;
import kr.leedox.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    @Autowired
    WordRepository wordRepository;

    public List<Wordbook> getList() {
        return wordRepository.findTop10ByOrderByUpdDateDesc();
    }

    public List<Wordbook> getList(String word) {
        return wordRepository.findTop10ByWordStartsWithOrderByWordAsc(word);
    }

    public Wordbook getWordbook(Integer id) {
        Optional<Wordbook> optionalWordbook =wordRepository.findById(id);
        Wordbook wordbook = null;

        if(optionalWordbook.isPresent()) {
            wordbook = optionalWordbook.get();
            //wordbook.setCrtDate(wordbook.getCrtDate().substring(0, 10));
        }

        return wordbook;
    }

    public Wordbook saveWordbook(Wordbook wordbook) {
        wordbook.setAccess(wordbook.getAccess() + 1);
        wordbook.setUpdDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return wordRepository.save(wordbook);
    }

    public void create(Wordbook wordbookForm) {
        Wordbook wordbook = Wordbook.builder()
                .word(wordbookForm.getWord())
                .meaning1(wordbookForm.getMeaning1())
                .crtDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .access(1)
                .build();
        wordRepository.save(wordbook);
    }

    public List<Wordbook> getListByMeaning(String key) {
        return wordRepository.findTop10ByMeaning1Containing(key);
    }

    public List<Wordbook> getListBySeq(String key) {
        return wordRepository.findTop10BySeqGreaterThanOrderBySeqAscIdAsc(Integer.parseInt(key));
    }

    public List<Wordbook> getListByTag(String key) {
        return wordRepository.findTop10ByMeaning2Containing(key);
    }
}
