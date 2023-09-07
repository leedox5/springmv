package kr.leedox.wordbook;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
            WordbookResponse wordbookResponse = new WordbookResponse(null, null, null, words, (List<WordMeaning>) wordbook.getWordMeanings(), null);
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
        WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), "eng", paging.getContent(), null, paging);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
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
            WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), "eng", words, null, null);

            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping( value = {"/search", "/search/{opt}", "/search/{opt}/{key}"})
    public ResponseEntity<?> search(@PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());

        // [2023.09.07] paging 처리
        //List<Wordbook> words = wordService.searchList(member, opt, key);
        Page<Wordbook> paging = wordService.searchListPaging(member, opt, key, 0);

        WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), opt.isPresent() ? opt.get() : "eng", paging.getContent(), null, paging);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    @GetMapping( value = {"/search1", "/search1/{opt}", "/search1/{opt}/{key}"})
    public ResponseEntity<?> search1(@PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        //List<Wordbook> words = wordService.searchList(opt, key);
        Page<Wordbook> paging = wordService.searchListPaging(null, opt, key, 0);

        WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), getOpts(), opt.isPresent() ? opt.get() : "eng", paging.getContent(), null, paging);
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
            WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), null, null, null, null, null);
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

        WordbookResponse wordbookResponse = new WordbookResponse(author.getMember().getUsername(), null, null, null, null, null);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

}
