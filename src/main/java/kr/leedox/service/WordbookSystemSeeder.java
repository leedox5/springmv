package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.WordMeaningRepository;
import kr.leedox.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WordbookSystemSeeder implements ApplicationRunner {
    private static final String SYSTEM_AUTHOR_EMAIL = "leedox@naver.com";

    private final WordRepository wordRepository;
    private final WordMeaningRepository wordMeaningRepository;
    private final MemberService memberService;

    @Override
    public void run(ApplicationArguments args) {
        Member member = memberService.getMember(SYSTEM_AUTHOR_EMAIL);

        seed(member, "10010", "소개", "Welcome to W-Book");
        seed(member, "10030", "redirect:/wordbook/", null);
        seed(member, "10050", "소개", "Welcome to T-Matches");
        seed(member, "202309.001", "컬럼정의", null);
        seedBookList(member);
    }

    private Wordbook seed(Member member, String word, String meaning, String detail) {
        Wordbook wordbook = wordRepository.findByWord(word).stream()
                .findFirst()
                .orElseGet(() -> createWordbook(member, word, meaning));

        if(wordbook.getAuthor() == null) {
            wordbook.setAuthor(member);
            wordbook = wordRepository.save(wordbook);
        }

        if(detail != null) {
            seedDetail(member, wordbook, detail);
        }

        return wordbook;
    }

    private Wordbook createWordbook(Member member, String word, String meaning) {
        String now = now();

        Wordbook wordbook = Wordbook.builder()
                .word(word)
                .seq(0)
                .meaning1(meaning)
                .crtDate(now)
                .updDate(now)
                .access(1)
                .author(member)
                .build();

        return wordRepository.save(wordbook);
    }

    private void seedDetail(Member member, Wordbook wordbook, String detail) {
        List<WordMeaning> wordMeanings = wordMeaningRepository.findByWordbook(wordbook);
        if(!wordMeanings.isEmpty()) {
            wordMeanings.stream()
                    .filter(wordMeaning -> wordMeaning.getMember() == null)
                    .forEach(wordMeaning -> {
                        wordMeaning.setMember(member);
                        wordMeaningRepository.save(wordMeaning);
                    });
            return;
        }

        String now = now();
        WordMeaning wordMeaning = WordMeaning.builder()
                .wordbook(wordbook)
                .meaning(detail)
                .crtDate(now)
                .updDate(now)
                .member(member)
                .build();

        wordMeaningRepository.save(wordMeaning);
    }

    private void seedBookList(Member member) {
        Wordbook wordbook = seed(member, "10110", "단어장 목록", null);
        String memberId = Integer.toString(member.getId());

        wordMeaningRepository.findByWordbook(wordbook).stream()
                .filter(wordMeaning -> isBookDefinition(wordMeaning, "10"))
                .findFirst()
                .ifPresentOrElse(
                        wordMeaning -> ensureBookMember(wordMeaning, memberId),
                        () -> createBookDefinition(member, wordbook, memberId)
                );
    }

    private boolean isBookDefinition(WordMeaning wordMeaning, String code) {
        String meaning = wordMeaning.getMeaning();
        if(meaning == null) {
            return false;
        }

        String[] values = meaning.split(",", 2);
        return values.length > 0 && code.equals(values[0]);
    }

    private void ensureBookMember(WordMeaning wordMeaning, String memberId) {
        String[] values = wordMeaning.getMeaning().split(",", -1);
        if(values.length < 5) {
            return;
        }

        boolean changed = false;
        String auth = appendId(values[2], memberId);
        if(!auth.equals(values[2])) {
            values[2] = auth;
            changed = true;
        }

        String active = appendId(values[3], memberId);
        if(!active.equals(values[3])) {
            values[3] = active;
            changed = true;
        }

        if(changed) {
            wordMeaning.setMeaning(String.join(",", values));
            wordMeaning.setUpdDate(now());
            wordMeaningRepository.save(wordMeaning);
        }
    }

    private void createBookDefinition(Member member, Wordbook wordbook, String memberId) {
        String now = now();
        WordMeaning wordMeaning = WordMeaning.builder()
                .wordbook(wordbook)
                .meaning("10,영한 기본," + memberId + "," + memberId + ",A")
                .crtDate(now)
                .updDate(now)
                .member(member)
                .build();

        wordMeaningRepository.save(wordMeaning);
    }

    private String appendId(String ids, String memberId) {
        if(ids == null || ids.isBlank()) {
            return memberId;
        }

        for(String id : ids.split("\\|")) {
            if(memberId.equals(id)) {
                return ids;
            }
        }

        return ids + "|" + memberId;
    }

    private String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
