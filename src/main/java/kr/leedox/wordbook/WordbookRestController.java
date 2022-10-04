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

    @GetMapping( value = {"/words", "/words/{id}"})
    public ResponseEntity<?> get(Principal principal, @PathVariable(required = false) Optional<Integer> id) {
        try {
            if(principal == null) {
                throw new Exception("LOGIN");
            }
            Member member = memberService.getMember(principal.getName());

            List<SearchOption> opts = new ArrayList<SearchOption>();
            opts.add(new SearchOption("eng", "단어"));
            opts.add(new SearchOption("kor", "의미"));

            List<Wordbook> words = wordService.getListByAuthor(member);
            WordbookResponse wordbookResponse = null;

            if(id.isPresent()) {
                List<Wordbook> filteredWordbook = words.stream().filter(x -> x.getId() == id.get()).collect(Collectors.toList());
                List<WordMeaning> wordMeanings = wordMeaningService.getWordMeanings(filteredWordbook.get(0));

                wordbookResponse = new WordbookResponse(member.getUsername(), opts, "eng", filteredWordbook, wordMeanings);
            } else {
                wordbookResponse = new WordbookResponse(member.getUsername(), opts, "eng", words, null);
            }

            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);

        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping( value = {"/search", "/search/{opt}", "/search/{opt}/{key}"})
    public ResponseEntity<?> search(@PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        List<Wordbook> words = wordService.searchList(member, opt, key);

        List<SearchOption> opts = new ArrayList<SearchOption>();
        opts.add(new SearchOption("eng", "단어"));
        opts.add(new SearchOption("kor", "의미"));

        WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), opts, opt.isPresent() ? opt.get() : "eng", words, null);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

    @GetMapping( value = {"/search1", "/search1/{opt}", "/search1/{opt}/{key}"})
    public ResponseEntity<?> search1(@PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        List<Wordbook> words = wordService.searchList(opt, key);

        List<SearchOption> opts = new ArrayList<SearchOption>();
        opts.add(new SearchOption("eng", "단어"));
        opts.add(new SearchOption("kor", "의미"));

        WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), opts, opt.isPresent() ? opt.get() : "eng", words, null);
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
            WordbookResponse wordbookResponse = new WordbookResponse(member.getUsername(), null, null, null, null);
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

        WordbookResponse wordbookResponse = new WordbookResponse(author.getMember().getUsername(), null, null, null, null);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }

}
