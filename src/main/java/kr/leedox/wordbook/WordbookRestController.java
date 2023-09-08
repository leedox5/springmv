package kr.leedox.wordbook;

import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.response.ResponseHandler;
import kr.leedox.service.MemberAdapter;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
public class WordbookRestController {
    @Autowired
    MemberService memberService;

    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @GetMapping("/words/{id}")
    public ResponseEntity<?> get(Principal principal, @PathVariable Integer id) {
        try {
            if(principal == null) {
                throw new Exception("LOGIN");
            }
            Wordbook wordbook = wordService.getWordbook(id);
            List<Wordbook> words = new ArrayList<>();
            words.add(wordbook);

            WordbookResponse wordbookResponse = WordbookResponse.builder()
                    .words(words)
                    .wordMeanings((List<WordMeaning>) wordbook.getWordMeanings())
                    .build();
            //WordbookResponse wordbookResponse = new WordbookResponse(null, null, null, words, (List<WordMeaning>) wordbook.getWordMeanings(), null);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
        } catch(Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping("/wordspage/{page}")
    public ResponseEntity<?> getPage(Principal principal, @PathVariable Integer page) {
        Member member = memberService.getMember(principal.getName());
        Page<Wordbook> paging = wordService.getListByAuthorPaging(member, page);
        paging.getContent();

        WordbookResponse wordbookResponse = WordbookResponse.builder()
                .username(member.getUsername())
                .opts(getOpts())
                .selOpt("eng")
                .words(paging.getContent())
                .paging(paging)
                .cols(getCols())
                .build();
        //WordbookResponse wordbookResponse1 = new WordbookResponse(member.getUsername(), getOpts(), "eng", paging.getContent(), null, paging);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    private List<Cols> getCols() {
        Cols cols1 = new Cols("id", "ID", true, "text-end");
        Cols cols2 = new Cols("word", "단어", true, "text-start");
        Cols cols3 = new Cols("meaning1", "의미", true, "text-start");

        List<Cols> cols =  List.of(cols1, cols2, cols3);

        Wordbook wordbook = wordService.getWordbookByWord("202309.001");

        return wordMeaningService.chkCols(wordbook, cols);
    }

    private List<SearchOption> getOpts() {
        List<SearchOption> opts = new ArrayList<SearchOption>();
        opts.add(new SearchOption("eng", "단어"));
        opts.add(new SearchOption("kor", "의미"));
        return opts;
    }

    @GetMapping("/words")
    public ResponseEntity<?> get(Principal principal) {
        try {
            if(principal == null) {
                throw new Exception("LOGIN");
            }
            Member member = memberService.getMember(principal.getName());

            List<Wordbook> words = wordService.getListByAuthor(member);

            WordbookResponse wordbookResponse = WordbookResponse.builder()
                    .username(member.getUsername())
                    .opts(getOpts())
                    .selOpt("eng")
                    .words(words)
                    .cols(getCols())
                    .build();

            //WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), "eng", words, null, null);

            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping( value = {"/search/{page}", "/search/{page}/{opt}", "/search/{page}/{opt}/{key}"})
    public ResponseEntity<?> search(@PathVariable(required = false) Optional<Integer> page,
                                    @PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());

        // [2023.09.07] paging 처리
        //List<Wordbook> words = wordService.searchList(member, opt, key);
        Page<Wordbook> paging = wordService.searchListPaging(member, opt, key, page.orElse(0));

        WordbookResponse wordbookResponse = WordbookResponse.builder()
                .username(member.getUsername())
                .opts(getOpts())
                .selOpt(opt.isPresent() ? opt.get() : "eng")
                .words(paging.getContent())
                .paging(paging)
                .cols(getCols())
                .build();
        //WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), opt.isPresent() ? opt.get() : "eng", paging.getContent(), null, paging);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    @GetMapping( value = {"/search1/{page}", "/search1/{page}/{opt}", "/search1/{page}/{opt}/{key}"})
    public ResponseEntity<?> search1(@PathVariable(required = false) Optional<Integer> page, @PathVariable(required = false) Optional<String> opt,
                                     @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        //List<Wordbook> words = wordService.searchList(opt, key);
        Page<Wordbook> paging = wordService.searchListPaging(null, opt, key, page.orElse(0));

        WordbookResponse wordbookResponse = WordbookResponse.builder()
                .username(member.getUsername())
                .opts(getOpts())
                .selOpt(opt.isPresent() ? opt.get() : "eng")
                .words(paging.getContent())
                .paging(paging)
                .cols(getCols())
                .build();

        //WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), opt.isPresent() ? opt.get() : "eng", paging.getContent(), null, paging);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createWord(@Valid @RequestBody WordbookForm wordbook, BindingResult bindingResult, Principal principal) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(bindingResult.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList()).get(0));
            }
            Member member = memberService.getMember(principal.getName());
            wordService.create(wordbook, member);

            WordbookResponse wordbookResponse = WordbookResponse.builder()
                    .username(member.getUsername())
                    .build();

            //WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), null, null, null, null, null);
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PostMapping(value = {"/save/{id}", "/save/{id}/{opt}", "/save/{id}/{opt}/{key}"})
    public ResponseEntity<?> saveWordbook(@PathVariable Integer id,
                               @PathVariable( required = false) Optional<String> opt,
                               @PathVariable( required = false) Optional<String> key,
                               @AuthenticationPrincipal MemberAdapter author,
                               Model model,
                               @RequestBody Wordbook wordbookForm) {
        Wordbook wordbookRepo = wordService.getWordbook(id);
        wordbookRepo.setWord(wordbookForm.getWord());
        wordbookRepo.setSeq(wordbookForm.getSeq());
        wordbookRepo.setMeaning1(wordbookForm.getMeaning1());
        wordbookRepo.setMeaning2(wordbookForm.getMeaning2());
        wordbookRepo.setExample1(wordbookForm.getExample1());
        wordbookRepo = wordService.saveWordbook(wordbookRepo);

        model.addAttribute("wordbook", wordbookRepo);
        model.addAttribute("author", author.getMember());

        String path = "";

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        }

        model.addAttribute("path", path);

        WordbookResponse wordbookResponse = WordbookResponse.builder()
                .username(author.getMember().getUsername())
                .build();

        //WordbookResponse wordbookResponse = new WordbookResponse(author.getMember().getUsername(), null, null, null, null, null);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    @PostMapping("/savemeaning/{wordbook_id}")
    public ResponseEntity<?> saveMeaning( @PathVariable Integer wordbook_id, @Valid @RequestBody WordMeaning wordMeaning, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        Wordbook wordbookRepo = wordService.getWordbook(wordbook_id);
        wordMeaningService.save(wordbookRepo, wordMeaning);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }


    @PostMapping("/meaning/save/{id}")
    public ResponseEntity<?> meaningSave( @PathVariable Integer id, @Valid @RequestBody WordMeaning wordMeaning, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        wordMeaningService.save(wordMeaning);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @GetMapping("/meaning/delete/{id}")
    public ResponseEntity<?> saveMeaning( @PathVariable Integer id) {
        WordMeaning wordMeaning = wordMeaningService.getWordMeaning(id);
        wordMeaningService.delete(wordMeaning);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @GetMapping("/word/delete/{id}")
    public ResponseEntity<?> deleteWord( @PathVariable Integer id) {
        wordService.delete(id);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }
}
