package kr.leedox.service;

import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.WordRepository;
import kr.leedox.wordbook.WordbookForm;
import kr.leedox.wordbook.WordbookSpcifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class WordService {
    @Autowired
    WordRepository wordRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    WordMeaningService wordMeaningService;

    public List<Wordbook> getList() {
        return wordRepository.findTop10ByOrderByUpdDateDesc();
    }

    public List<Wordbook> getListAll() {
        return wordRepository.findAll();
    }

    public List<Wordbook> getList(String word) {
        return wordRepository.findTop10ByWordStartsWithOrderByWordAsc(word);
    }

    public Wordbook getWordbook(Integer id) {
        Optional<Wordbook> optionalWordbook = wordRepository.findById(id);
        Wordbook wordbook = null;

        if(optionalWordbook.isPresent()) {
            wordbook = optionalWordbook.get();
            //wordbook.setCrtDate(wordbook.getCrtDate().substring(0, 10));
        }

        return wordbook;
    }

	public Wordbook getWordbookByWord(String word) {
        Wordbook wordbook = null;

        if("10010".equals(word)) {
            checkInitData(word, "소개");
            checkDetailData(word, "Welcome to W-Book");
        }

        if("10030".equals(word)) {
            checkInitData(word, "redirect:/wordbook/");
        }

        if("10050".equals(word)) {
            checkInitData(word, "소개");
            checkDetailData(word, "Welcome to T-Matches");
        }

        if("202309.001".equals(word)) {
            checkInitData(word, "컬럼정의");
        }
		List<Wordbook> wordbookList = wordRepository.findByWord(word);

        return wordbookList.get(0);
	}

    private void checkDetailData(String word, String detail) {
        Wordbook wordbook = wordRepository.findByWord(word).get(0);
        List<WordMeaning> wordMeanings = wordMeaningService.getWordMeanings(wordbook);
        if(wordMeanings.isEmpty()) {
            WordMeaning wordMeaning = new WordMeaning();
            wordMeaning.setMeaning(detail);
            wordMeaningService.save(wordbook, wordMeaning);
        }
    }

    private void checkInitData(String word, String meaning) {
        List<Wordbook> wordbookList = wordRepository.findByWord(word);
        if(wordbookList.isEmpty()) {
            WordbookForm form = new WordbookForm();
            form.setWord(word);
            form.setMeaning1(meaning);
            form.setSeq(0);

            Member member = memberService.getMember("leedox@naver.com");

            create(form, member);
        }
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

    public void create(WordbookForm wordbookForm, Member member) {
        Wordbook wordbook = Wordbook.builder()
                .word(wordbookForm.getWord())
                .seq(wordbookForm.getSeq())
                .meaning1(wordbookForm.getMeaning1())
                .crtDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .access(1)
                .author(member)
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
        return wordRepository.findByMeaning2ContainingOrderByWordAsc(key);
    }

    public List<Wordbook> getListByAuthor(Member author) {
        return wordRepository.findByAuthorOrderByUpdDateDesc(author);
    }

    public Page<Wordbook> getListByAuthorPaging(Member member, Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return wordRepository.findByAuthorOrderByUpdDateDesc(member, pageable);
    }

    public Page<Wordbook> searchListPaging(Member author, Optional<String> opt, Optional<String> key, Integer page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("updDate").descending());

        Specification<Wordbook> spec = null;

        if(author == null) {
            spec = Specification.where(WordbookSpcifications.equalToSeq(-1));
        } else {
            spec = Specification.where(WordbookSpcifications.equalToAuthor(author));
        }

        String option = opt.isPresent() ? opt.get() : "";

        if("eng".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeWord(key.get()));
            }
        } else if("kor".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning1(key.get()));
            }
        } else if("num".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.greaterThanSeq(key.get()));
            }
        } else if("tag".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning2(key.get()));
            }
        }
        return wordRepository.findAll(spec, pageable);
    }

    public List<Wordbook> searchList(Member author, Optional<String> opt, Optional<String> key) {
        Specification<Wordbook> spec = Specification.where(WordbookSpcifications.equalToAuthor(author));

        String option = opt.isPresent() ? opt.get() : "";

        if("eng".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeWord(key.get()));
            }
        } else if("kor".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning1(key.get()));
            }
        } else if("num".equals(option)) {
			if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.greaterThanSeq(key.get()));
			}
        } else if("tag".equals(option)) {
			if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning2(key.get()));
			}
        }
        return wordRepository.findAll(spec, Sort.by("updDate").descending());
    }

    public List<Wordbook> bookList(String code, String sort, Optional<String> opt, Optional<String> key) {
        Specification<Wordbook> spec = Specification.where(WordbookSpcifications.equalToMeaning2(code));

        String option = opt.isPresent() ? opt.get() : "";

        if("eng".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeWord(key.get()));
            }
        } else if("kor".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning1(key.get()));
            }
        } else if("num".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.greaterThanSeq(key.get()));
            }
        } else if("tag".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning2(key.get()));
            }
        }
        if("A".equals(sort)) {
            return wordRepository.findAll(spec, Sort.by("word").ascending());
        } else {
            return wordRepository.findAll(spec, Sort.by("word").descending());
        }
    }

    public List<Wordbook> searchList(Optional<String> opt, Optional<String> key) {
        Specification<Wordbook> spec = Specification.where(WordbookSpcifications.equalToSeq(-1));

        String option = opt.isPresent() ? opt.get() : "";

        if("eng".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeWord(key.get()));
            }
        } else if("kor".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning1(key.get()));
            }
        } else if("num".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.greaterThanSeq(key.get()));
            }
        } else if("tag".equals(option)) {
            if(key.isPresent()) {
                spec = spec.and(WordbookSpcifications.likeMeaning2(key.get()));
            }
        }
        return wordRepository.findAll(spec, Sort.by("word").ascending());
    }

    public Page<Wordbook> getPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return wordRepository.findAll(pageable);
    }

    public void delete(Integer id) {
        Wordbook wordbook = getWordbook(id);
        wordRepository.delete(wordbook);
    }
}
