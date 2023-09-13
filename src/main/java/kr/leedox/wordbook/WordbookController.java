package kr.leedox.wordbook;

import kr.leedox.entity.Member;
import kr.leedox.entity.Wordbook;
import kr.leedox.controller.Open;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordService;
import kr.leedox.wordbook.WordbookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/wordbook")
public class WordbookController {

    @Autowired
    MemberService memberService;

    @Autowired
    WordService wordService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String home() {
        return "thymeleaf/wordbook/words";
    }

    @GetMapping(value = {"/words", "/words/{opt}", "/words/{opt}/{key}"})
    public String words(@PathVariable(required = false) Optional<String> opt,
                        @PathVariable(required = false) Optional<String> key, Model model) {
        String path = "";

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        } else {
            model.addAttribute("key", "");
        }
        model.addAttribute("path", path);

        return "thymeleaf/wordbook/list";
    }

    @GetMapping(value = {"/word/{id}", "/word/{id}/{opt}", "/word/{id}/{opt}/{key}"})
    public String word(@PathVariable Integer id,
                        @PathVariable(required = false) Optional<String> opt,
                        @PathVariable(required = false) Optional<String> key, Model model) {
        model.addAttribute("id", id);

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

        return "thymeleaf/wordbook/detail";
    }

    @GetMapping(value = {"/worddiv/{id}", "/worddiv/{id}/{opt}", "/worddiv/{id}/{opt}/{key}"})
    public String wordDiv(@PathVariable Integer id,
                       @PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key, Model model) {
        model.addAttribute("id", id);

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

        return "thymeleaf/wordbook/detail_div";
    }

    @GetMapping(value = {"/list", "/list/{opt}", "/list/{opt}/{key}"})
    public String list(@PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key, Model model, Principal principal) {

        Member author = memberService.getMember(principal.getName());
        List<Wordbook> words = wordService.searchList(author, opt, key);

        String path = "";

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        }
        model.addAttribute("key", key.isPresent() ? key.get() : "");
        model.addAttribute("list", words);
        model.addAttribute("path", path);
        model.addAttribute("author", author);

        return "thymeleaf/word_list";
    }

    @GetMapping("/intro")
    public String getIntro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10010");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/intro_div";
    }

    @GetMapping("/add")
    public String add(WordbookForm wordbookForm, Model model) {
        List<Open> opens = new ArrayList<>();
        Open open1 = Open.builder().id(1).name("공개").val(-1).build();
        Open open2 = Open.builder().id(2).name("비공개").val(0).build();

        opens.add(open1);
        opens.add(open2);

        model.addAttribute("opens", opens);

        return "thymeleaf/wordbook/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String createWordbook(@Valid WordbookForm wordbookForm, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            List<Open> opens = new ArrayList<>();
            Open open1 = Open.builder().id(1).name("공개").val(-1).build();
            Open open2 = Open.builder().id(2).name("비공개").val(0).build();

            opens.add(open1);
            opens.add(open2);
            model.addAttribute("opens", opens);

            return "thymeleaf/wordbook/create";
        }
        Member member = memberService.getMember(principal.getName());
        wordService.create(wordbookForm, member);
        return "redirect:/";
    }

}
