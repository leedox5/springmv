package kr.leedox.service;

import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.WordMeaningRepository;
import kr.leedox.wordbook.Cols;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    public WordMeaning save(WordMeaning wordMeaningForm) {
        Optional<WordMeaning> owm = wordMeaningRepository.findById(wordMeaningForm.getId());
        WordMeaning wordMeaning = null;
        if (owm.isPresent()) {
            wordMeaning = owm.get();
            wordMeaning.setMeaning(wordMeaningForm.getMeaning());
            wordMeaning.setUpdDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return wordMeaningRepository.save(wordMeaning);
        }
        return null;
    }

    public WordMeaning getWordMeaning(Integer id) {
        Optional<WordMeaning> optionalWordMeaning =wordMeaningRepository.findById(id);
        WordMeaning wordMeaning = null;

        if(optionalWordMeaning.isPresent()) {
            wordMeaning = optionalWordMeaning.get();
        }
        return wordMeaning;
    }

    public List<WordMeaning> getWordMeanings(Wordbook wordbook) {
        return wordMeaningRepository.findByWordbook(wordbook);
    }
    public void delete(WordMeaning wordMeaning) {
        wordMeaningRepository.delete(wordMeaning);
    }

    public List<Cols> chkCols(Wordbook wordbook, List<Cols> cols) {
        List<WordMeaning> wordMeanings = getWordMeanings(wordbook);
        List<Cols> colsDb = new ArrayList<>();

        if(wordMeanings.isEmpty()) {
            for(Cols col : cols) {
                String str = col.getId() + "," + col.getHeader() + "," + col.isVisible() + "," + col.getAlign();
                WordMeaning wordMeaning = WordMeaning.builder().meaning(str).build();
                save(wordbook, wordMeaning);
            }
            wordMeanings = getWordMeanings(wordbook);
        }

        for(WordMeaning meaning : wordMeanings) {
            String[] means = meaning.getMeaning().split(",");

            Cols col = new Cols(means[0], means[1], Boolean.parseBoolean(means[2]), means[3]);
            colsDb.add(col);
        }
        return colsDb;
    }

    public String getBookName(Wordbook wordbook, String code) {
        for(WordMeaning wordMeaning : wordbook.getWordMeanings()) {
            String[] str = wordMeaning.getMeaning().split(",");
            if(code.equals(str[0])) {
                return str[1];
            }
        }
        return null;
    }
}
