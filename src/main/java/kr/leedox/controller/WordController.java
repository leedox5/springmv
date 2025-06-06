package kr.leedox.controller;

import kr.leedox.entity.Member;
import kr.leedox.entity.Role;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.*;
import kr.leedox.wordbook.WordbookForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class WordController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @Autowired
    MemberService memberService;

    @Autowired
    RoleService roleService;
    /*
    @GetMapping("/wordbook")
    public String getList(Model model) {
        List<Wordbook> words = wordService.getList();
        model.addAttribute("list", words);
        model.addAttribute("path","");
        return "WordbookList";
    }
    */

    @PostMapping("/wordbook1")
    public String getList(Model model, Wordbook wordbook) {
        List<Wordbook> words = null;

        if(wordbook.getWord().isEmpty()) {
            words = wordService.getList();
        } else {
            words = wordService.getList(wordbook.getWord());
            model.addAttribute("key", wordbook.getWord());
        }
        model.addAttribute("list", words);
        return "WordbookList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wordbook2")
    public String getList2(Model model, @RequestParam(value = "page", defaultValue = "0") Integer page
                                      , @RequestParam(value = "opt", defaultValue = "eng") String opt
                                      , @RequestParam(value = "key", defaultValue = "") String key
                                      , Principal principal) {
        Page<Wordbook> words = null;
        Member author = memberService.getMember(principal.getName());
        //Page<Wordbook> words = wordService.getListByAuthorPaging(author, page);
        words = wordService.searchListPaging(author, "00", "A", Optional.of(opt), Optional.ofNullable(key), page);

        model.addAttribute("list", words);
        model.addAttribute("page", page);
        model.addAttribute("opt", opt);
        model.addAttribute("key", key);
        model.addAttribute("author", author);
        return "thymeleaf/word_list";
    }

    @PostMapping("/wordpost")
    public String wordPost() {
        return "Hello, POST";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/wordbook2")
    public String getListOpt(Model model, @RequestParam MultiValueMap<String, String> formData, Principal principal) {
        Page<Wordbook> words = null;

        String opt = formData.getFirst("opt");
        String key = formData.getFirst("key");

        String path = opt.isEmpty() ? "" : opt + "/" + key;

        Member author = memberService.getMember(principal.getName());

        words = wordService.searchListPaging(author, "00", "A", Optional.of(opt), Optional.ofNullable(key), 0);

        model.addAttribute("list", words);
		model.addAttribute("opt", opt);
        model.addAttribute("key", key);
        model.addAttribute("page", 0);
        model.addAttribute("author", author);
        return "thymeleaf/word_list";
    }

    @GetMapping("/wordbook1/{id}")
    public String getWordbook1(@PathVariable Integer id, Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);
        return "WordbookDetail";
    }

	@GetMapping("/")
	public String getHome() {
        /* ---
		Wordbook wordbook = wordService.getWordbookByWord("10030");
        if(wordbook == null) {
            return "redirect:/wordhome";
        }
		return wordbook.getMeaning1();
        --- */
        return "thymeleaf/landing";
	}

    @GetMapping("/wordhome")
    public String home(@AuthenticationPrincipal UserDetails user) {
        if (user != null) {
            logger.trace("user: {}", user.getUsername());
            logger.trace("auth: {}", user.getAuthorities());
            return "redirect:/wordbook2";
        }
        return "redirect:/intro";
    }

    @GetMapping("/intro")
    public String getIntro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10010");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/intro";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10020");
        if(wordbook == null) {
            return "thymeleaf/login";
        }
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/intro";
    }

    /*
	@GetMapping( value = {"/wordbook/{id}", "/wordbook/{id}/{opt}", "/wordbook/{id}/{opt}/{key}"})
    public String getWordbook(@PathVariable Integer id,
                              @PathVariable(required = false) Optional<String> opt,
                              @PathVariable(required = false) Optional<String> key, Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);

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

        return "WordbookDetail";
    }
    */

    @PreAuthorize("isAuthenticated()")
    @GetMapping( value = {"/wordbook2/{id}", "/wordbook2/{id}/{opt}", "/wordbook2/{id}/{opt}/{key}"})
    public String getWordbook2(@PathVariable Integer id,
                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "opt", defaultValue = "eng") String opt,
                               @RequestParam(value = "key", defaultValue = "") String key,
                               @AuthenticationPrincipal MemberAdapter author,
                               Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);
        model.addAttribute("author", author.getMember());
        model.addAttribute("page", page);
        model.addAttribute("opt", opt);
        model.addAttribute("key", key);

        return "thymeleaf/word_detail";
    }

    @PostMapping(value = {"/wordbook2/save/{id}", "/wordbook2/save/{id}/{opt}", "/wordbook2/save/{id}/{opt}/{key}"})
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

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        }

        model.addAttribute("path", path);

        return "thymeleaf/word_detail";
    }

    @PostMapping( value = {"/wordbook/savemeaning/{id}", "/wordbook/savemeaning/{id}/{opt}", "/wordbook/savemeaning/{id}/{opt}/{key}"})
    public String saveWordMeaning(@PathVariable Integer id,
                                  @PathVariable(required = false) Optional<String> opt,
                                  @PathVariable(required = false) Optional<String> key,
                                  Model model,
                                  WordMeaning wordMeaningForm) {
        Wordbook wordbookRepo = wordService.getWordbook(id);
        wordMeaningService.save(wordbookRepo, wordMeaningForm);

        model.addAttribute("wordbook", wordbookRepo);

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

        return "WordbookDetail";
    }

    @PostMapping( value = {"/wordbook2/savemeaning/{id}", "/wordbook2/savemeaning/{id}/{opt}", "/wordbook2/savemeaning/{id}/{opt}/{key}"})
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

        if (opt.isPresent()) {
            model.addAttribute("opt", opt.get());
            path = opt.get();
        }

        if (key.isPresent()) {
            model.addAttribute("key", key.get());
            path += "/" + key.get();
        }

        model.addAttribute("path", path);

        return "thymeleaf/word_detail";
    }

    @GetMapping(value = {"/wordbook/deletemeaning/{id}", "/wordbook/deletemeaning/{id}/{opt}", "/wordbook/deletemeaning/{id}/{opt}/{key}"})
    public String deleteWordMeaning(@PathVariable Integer id,
                                    @PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key,
                                    Model model) {
        WordMeaning wordMeaning = wordMeaningService.getWordMeaning(id);
        wordMeaningService.delete(wordMeaning);
        model.addAttribute("wordbook", wordMeaning.getWordbook());

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
        return "WordbookDetail";
    }

    @GetMapping("/wordbook/create")
    public String createWord() {
        return "WordbookForm";
    }

    /*
    @PostMapping("/wordbook/create")
    public RedirectView createGame(Wordbook wordbook) {
        wordService.create(wordbook);
        return new RedirectView("/wordbook");
    }
    */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wordbook2/create")
    public String createWord2(WordbookForm wordbookForm, Model model) {
        List<Open> opens = new ArrayList<>();
        Open open1 = Open.builder().id(1).name("공개").val(-1).build();
        Open open2 = Open.builder().id(2).name("비공개").val(0).build();

        opens.add(open1);
        opens.add(open2);

        model.addAttribute("opens", opens);
        return "thymeleaf/wordbook_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/wordbook2/create")
    public String createWordbook(@Valid WordbookForm wordbookForm, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            List<Open> opens = new ArrayList<>();
            Open open1 = Open.builder().id(1).name("공개").val(-1).build();
            Open open2 = Open.builder().id(2).name("비공개").val(0).build();

            opens.add(open1);
            opens.add(open2);
            model.addAttribute("opens", opens);
            return "thymeleaf/wordbook_form";
        }
        Member member = memberService.getMember(principal.getName());
        wordService.create(wordbookForm, member);
        return "redirect:/wordbook2";
    }

}
