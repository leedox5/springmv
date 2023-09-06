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

		List<Wordbook> wordbookList = wordRepository.findByWord(word);

		if(wordbookList.isEmpty()) {
            WordbookForm form = new WordbookForm();
            form.setWord("10050");
            form.setMeaning1("소개");
            form.setSeq(0);
            Member member = memberService.getMember(1);
            this.create(form, member);
            wordbook = wordRepository.findByWord(word).get(0);
            WordMeaning wordMeaning = new WordMeaning();
            wordMeaning.setMeaning("Welcome to T-Matches");
            wordMeaningService.save(wordbook, wordMeaning);
            return wordbook;
		} else {
            return wordbookList.get(0);
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

}
