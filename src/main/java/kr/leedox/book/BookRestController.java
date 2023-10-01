package kr.leedox.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.response.ResponseHandler;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import kr.leedox.wordbook.Cols;
import kr.leedox.wordbook.SearchOption;
import kr.leedox.wordbook.WordbookResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
public class BookRestController {

    private final ObjectMapper objectMapper;

    @Autowired
    MemberService memberService;

    @Autowired
    WordService wordService;

    @Autowired
    BookService bookService;

    @Autowired
    WordMeaningService wordMeaningService;
    public BookRestController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping( value = {"/data/my", "/data/my/{opt}", "/data/my/{opt}/{key}", "/data/my/{opt}/{key}/{page}"})
    public ResponseEntity<?> search(@PathVariable(required = false) Optional<Integer> page,
                                    @PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {

        try {
            if(principal == null) {
                throw new Exception("LOGIN");
            }
            Member member = memberService.getMember(principal.getName());

            Page<Wordbook> paging = wordService.searchListPaging(member, "00", "A", opt, key, page.orElse(0));

            List<Wordbook> words = paging.getContent();

            /*
            Book book = Book.builder().id(10).name("영한기본").build();
            words.get(0).setBooks(List.of(book));
            */

            Wordbook wordbook = wordService.getWordbookByWord("10110");
            List<Book> books = bookService.getBooks(wordbook, member.getId());

            for (int i = 0; i < words.size(); i++) {
                words.get(i).setBooks(bookService.getBooksByWord(books, words.get(i).getWord()));
            }

            WordbookResponse wordbookResponse = WordbookResponse.builder()
                    .username(member.getUsername())
                    .opts(getOpts())
                    .selOpt(opt.orElse("eng"))
                    .words(words)
                    .paging(paging)
                    .cols(getCols())
                    .build();
            return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);

        } catch(Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping( value = {"/data/list/{code}/{sort}", "/data/list/{code}/{sort}/{opt}", "/data/list/{code}/{sort}/{opt}/{key}", "/data/list/{code}/{sort}/{opt}/{key}/{page}"})
    public ResponseEntity<?> books(@PathVariable String code,
                                   @PathVariable String sort,
                                   @PathVariable(required = false) Optional<String> opt,
                                   @PathVariable(required = false) Optional<String> key,
                                   @PathVariable(required = false) Optional<Integer> page,
                                   Principal principal) {
        Member member = memberService.getMember(principal.getName());
        Page<Wordbook> paging = wordService.searchListPaging(null, code, sort, opt, key, page.orElse(0));
        WordbookResponse wordbookResponse = WordbookResponse.builder()
                .username(member.getUsername())
                .opts(getOpts())
                .selOpt(opt.orElse("eng"))
                .words(paging.getContent())
                .paging(paging)
                .cols(getCols())
                .build();
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, wordbookResponse);
    }


    @PostMapping("/create/meaning")
    public ResponseEntity<?> createMeaning(@Valid @RequestBody WordMeaningDTO wordMeaningDTO, Errors errors, Principal principal) throws JsonProcessingException {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }

        Member member = memberService.getMember(principal.getName());

        String requestJson = objectMapper.writeValueAsString(wordMeaningDTO);
        System.out.println(requestJson);

        Wordbook wordbook = wordService.getWordbook(wordMeaningDTO.getWordbookId());
        wordService.saveWordbook(wordbook);

        wordMeaningService.create(wordMeaningDTO, wordbook, member);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/create/meaning/{id}")
    public ResponseEntity<?> createReply(@Valid @RequestBody WordMeaningDTO wordMeaningDTO, Errors errors, @PathVariable Integer id, Principal principal) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        Member member = memberService.getMember(principal.getName());
        WordMeaning upper = wordMeaningService.getWordMeaning(id);

        wordMeaningService.create(wordMeaningDTO, upper, member);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/save/meaning")
    public ResponseEntity<?> saveMeaning(@Valid @RequestBody WordMeaningDTO wordMeaningDTO, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        Wordbook wordbook = wordService.getWordbook(wordMeaningDTO.getWordbookId());
        wordService.saveWordbook(wordbook);

        wordMeaningService.save(wordMeaningDTO);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    private List<SearchOption> getOpts() {
        List<SearchOption> opts = new ArrayList<SearchOption>();
        opts.add(new SearchOption("eng", "단어"));
        opts.add(new SearchOption("kor", "의미"));
        return opts;
    }

    private List<Cols> getCols() {
        Cols cols1 = new Cols("id", "ID", true, "text-end");
        Cols cols2 = new Cols("word", "단어", true, "text-start");
        Cols cols3 = new Cols("meaning1", "의미", true, "text-start");

        List<Cols> cols =  List.of(cols1, cols2, cols3);

        Wordbook wordbook = wordService.getWordbookByWord("202309.001");

        return wordMeaningService.chkCols(wordbook, cols);
    }

}
