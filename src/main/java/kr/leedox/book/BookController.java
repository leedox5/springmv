package kr.leedox.book;

import kr.leedox.controller.Open;
import kr.leedox.entity.Member;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.MemberAdapter;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import kr.leedox.wordbook.WordbookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.http.HttpHeaders;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @Autowired
    MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tit", "내단어장");
        model.addAttribute("path", "/data/wordbook");
        return "thymeleaf/book/list";
    }

    @GetMapping("/intro")
    public String intro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10010");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/book/intro";
    }

    @GetMapping("/view/{word}")
    public String getWordbook(@PathVariable String word, Model model) {
        Wordbook wordbook = wordService.getWordbookByWord(word);
        model.addAttribute("tit", wordbook.getMeaning1());
        model.addAttribute("wordbook", wordbook);

        List<Book> books = new ArrayList<>();
        for(WordMeaning wordMeaning : wordbook.getWordMeanings()) {
            String[] str = wordMeaning.getMeaning().split(",");
            Book book = Book.builder().code(str[0]).name(str[1]).build();
            books.add(book);
        }

        model.addAttribute("books", books);

        return "thymeleaf/book/word";
    }

    @GetMapping( value = {"/search/{opt}", "/search/{opt}/{key}"})
    public String search(@PathVariable String opt
                        ,@PathVariable(required = false) Optional<String> key, Model model) {

        String loc = "/data/wordbook/" + opt;

        model.addAttribute("tit", "내단어장");
        model.addAttribute("opt", opt);

        if(key.isPresent()) {
            model.addAttribute("key", key.get());
            loc += "/" + key.get();
        } else {
            model.addAttribute("key", "");
        }
        model.addAttribute("path", loc);
        return "thymeleaf/book/list";
    }

    @GetMapping( value = {"/books/{id}/{opt}/{key}"})
    public String books(@PathVariable String id
                       ,@PathVariable(required = false) Optional<String> opt
                       ,@PathVariable(required = false) Optional<String> key,Model model) {
        model.addAttribute("tit", "내단어장");
        model.addAttribute("opt", opt.get());
        model.addAttribute("key", key.get());
        model.addAttribute("path", "/data/books/" + id + "/" + opt.get() + "/" + key.get());
        return "thymeleaf/book/books";
    }

    @GetMapping(value = {"/word/{cate}/{id}", "/word/{cate}/{id}/{opt}", "/word/{cate}/{id}/{opt}/{key}"})
    public String word(@PathVariable Integer id,
                       @PathVariable String cate,
                       @PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key, Model model) {

        Wordbook wordbook = wordService.getWordbook(id);

        String path = "";

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path += opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        }
        model.addAttribute("id", id);
        model.addAttribute("cate", cate);
        model.addAttribute("wordbook", wordbook);
        model.addAttribute("path", path);
        return "thymeleaf/book/word_detail";
    }

    @PostMapping( value = {"/word/{id}/savemeaning", "/word/{id}/savemeaning/{opt}", "/word/{id}/savemeaning/{opt}/{key}"})
    public String saveWordMeaning2(@PathVariable Integer id,
                                   @PathVariable(required = false) Optional<String> opt,
                                   @PathVariable(required = false) Optional<String> key,
                                   @AuthenticationPrincipal MemberAdapter author,
                                   Model model,
                                   WordMeaning wordMeaningForm) {
        Wordbook wordbookRepo = wordService.getWordbook(id);
        wordMeaningService.save(wordbookRepo, wordMeaningForm);

        model.addAttribute("wordbook", wordbookRepo);
        model.addAttribute("author", author.getMember());

        String path = "";
        String loc = "/book/word/" + id;

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
            loc += "/" + opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
            loc += "/" + key.get();
        }

        model.addAttribute("path", path);

        return "redirect:" + loc;
    }

    @GetMapping(value = {"/list/{code}", "/list/{code}/{opt}", "/list/{code}/{opt}/{key}"})
    public String list(@PathVariable String code
                      ,@PathVariable(required = false) Optional<String> opt
                      ,@PathVariable(required = false) Optional<String> key, Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10110");
        String name = wordMeaningService.getBookName(wordbook, code);

        model.addAttribute("code", code);
        model.addAttribute("tit", name);

        String path = "";

        if (opt.isPresent()) {
            path = "/" + opt.get();
        }

        if (key.isPresent()) {
            path += "/" + key.get();
        }
        model.addAttribute("opt", opt.orElse(""));
        model.addAttribute("key", key.orElse(""));
        model.addAttribute("path", "/data/books/" + code + path);
        return "thymeleaf/book/books";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String createWord2(WordbookForm wordbookForm, Model model) {
        List<Open> opens = new ArrayList<>();
        Open open1 = Open.builder().id(1).name("공개").val(-1).build();
        Open open2 = Open.builder().id(2).name("비공개").val(0).build();

        opens.add(open1);
        opens.add(open2);

        model.addAttribute("opens", opens);
        return "thymeleaf/book/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createWordbook(@Valid WordbookForm wordbookForm, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            List<Open> opens = new ArrayList<>();
            Open open1 = Open.builder().id(1).name("공개").val(-1).build();
            Open open2 = Open.builder().id(2).name("비공개").val(0).build();

            opens.add(open1);
            opens.add(open2);
            model.addAttribute("opens", opens);
            return "thymeleaf/book/create";
        }
        Member member = memberService.getMember(principal.getName());
        wordService.create(wordbookForm, member);
        return "redirect:/book/";
    }

    @PostMapping(value = {"/wordbook/{id}/save", "/wordbook/{id}/{opt}/save", "/wordbook/{id}/{opt}/{key}/save"})
    public String saveWordbook(@PathVariable Integer id,
                               @PathVariable( required = false) Optional<String> opt,
                               @PathVariable( required = false) Optional<String> key,
                               @AuthenticationPrincipal MemberAdapter author,
                               Model model,
                               Wordbook wordbookForm) {
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
        String loc = "/book/word/" + id;

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
            loc += "/" + opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
            loc += "/" + key.get();
        }

        model.addAttribute("path", path);

        return "redirect:" + loc;
    }

}
