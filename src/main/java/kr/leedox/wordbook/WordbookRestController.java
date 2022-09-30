package kr.leedox.wordbook;

import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.response.ResponseHandler;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
