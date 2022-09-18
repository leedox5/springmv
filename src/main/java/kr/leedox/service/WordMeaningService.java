package kr.leedox.service;

import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.WordMeaningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WordMeaningService {
    @Autowired
    WordMeaningRepository wordMeaningRepository;

    public WordMeaning save(Wordbook wordbookRepo, WordMeaning wordMeaningForm) {
        Optional<WordMeaning> owm = wordMeaningRepository.findById(wordMeaningForm.getId());
        WordMeaning wordMeaning = null;
        if(owm.isPresent()) {
            wordMeaning = owm.get();
            wordMeaning.setWordbook(wordbookRepo);
            wordMeaning.setMeaning(wordMeaningForm.getMeaning());
            wordMeaning.setUpdDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            wordMeaning = WordMeaning.builder()
                            .wordbook(wordbookRepo)
                            .meaning(wordMeaningForm.getMeaning())
                            .crtDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .updDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .build();
        }
        return wordMeaningRepository.save(wordMeaning);
    }

    public WordMeaning getWordbook(Integer id) {
        Optional<WordMeaning> optionalWordMeaning =wordMeaningRepository.findById(id);
        WordMeaning wordMeaning = null;

        if(optionalWordMeaning.isPresent()) {
            wordMeaning = optionalWordMeaning.get();
        }
        return wordMeaning;
    }

    public void delete(WordMeaning wordMeaning) {
        wordMeaningRepository.delete(wordMeaning);
    }
}
