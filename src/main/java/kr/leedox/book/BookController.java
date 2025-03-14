package kr.leedox.book;

import kr.leedox.controller.Open;
import kr.leedox.entity.Member;
import kr.leedox.entity.UserCreateForm;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.MemberAdapter;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import kr.leedox.wordbook.PaypalConfig;
import kr.leedox.wordbook.WordCountDTO;
import kr.leedox.wordbook.WordbookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @Autowired
    MemberService memberService;

    @Autowired
    BookService bookService;

    private final PaypalConfig paypalConfig;

    public BookController(PaypalConfig paypalConfig) {
        this.paypalConfig = paypalConfig;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails user, @RequestParam(required = false) String lang, HttpSession session) {
        /* ---
        model.addAttribute("tit", "내단어장");
        model.addAttribute("path", "/data/wordbook");
        --- */
        if(lang != null) {
            if(lang.equals("en")) {
                session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.ENGLISH);
            } else {
                session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.KOREA);
            }
        }

        if(user == null) {
            return "redirect:/book/login";
        }
        return "redirect:/book/my";
    }

    @GetMapping("/intro")
    public String intro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10010");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/book/intro";
    }

    @GetMapping("/signup")
    public String signup(Model model, UserCreateForm userCreateForm) {
        return "thymeleaf/book/signup";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "thymeleaf/book/login";
    }

    @GetMapping("/means/{word}")
    public String getMeanings(@PathVariable String word, Model model) {
        Wordbook wordbook = wordService.getWordbookByWord(word);
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/book/means";
    }

    @GetMapping("/stat/{cate}")
    public String getStat(@PathVariable String cate, Model model) {
        List<WordCountDTO> wordCountDTOS = wordService.getWordCount(cate);
        model.addAttribute("title", "Statistics");
        model.addAttribute("wordcounts", wordCountDTOS);
        return "thymeleaf/book/stat";
    }
    @GetMapping("/view/{word}")
    public String getWordbook(@PathVariable String word, Model model, @AuthenticationPrincipal MemberAdapter author ) {

        Wordbook wordbook = wordService.getWordbookByWord(word);
        model.addAttribute("tit", wordbook.getMeaning1());
        model.addAttribute("wordbook", wordbook);

        List<Book> books = bookService.getBooks(wordbook, author.getMember().getId());
        /* ---
        for(WordMeaning wordMeaning : wordbook.getWordMeanings()) {
            String[] str = wordMeaning.getMeaning().split(",");
            Book book = Book.builder().code(str[0]).name(str[1]).build();
            books.add(book);
        }
        --- */
        model.addAttribute("books", books);

        return "thymeleaf/book/word";
    }

    @GetMapping( value = {"/my", "/my/{opt}", "/my/{opt}/{key}", "/my/{opt}/{key}/{page}"})
    public String list(@PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key,
                       @PathVariable(required = false) Optional<Integer> page, Model model) {

        model.addAttribute("tit", "MY BOOK");
        model.addAttribute("opt", opt.orElse("eng"));
        model.addAttribute("key", key.orElse(""));
        model.addAttribute("page", page.orElse(0));

        return "thymeleaf/book/list";
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

    @GetMapping(value = {"/word/{word}"})
    public String getWord(@PathVariable String word, Model model) {
        Wordbook wordbook = wordService.getWordbookByWord(word);
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/book/word_view";
    }

    @GetMapping(value = {"/word/{cate}/{sort}/{id}", "/word/{cate}/{sort}/{id}/{opt}", "/word/{cate}/{sort}/{id}/{opt}/{key}", "/word/{cate}/{sort}/{id}/{opt}/{key}/{page}"})
    public String word(@PathVariable Integer id,
                       @PathVariable String cate,
                       @PathVariable String sort,
                       @PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key,
		               @PathVariable(required = false) Optional<Integer> page, Model model) {

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
        model.addAttribute("sort", sort);
        model.addAttribute("wordbook", wordbook);
        model.addAttribute("path", path);
        model.addAttribute("newline", "\n");
		model.addAttribute("page", page.orElse(0));

        return "thymeleaf/book/word_detail";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "thymeleaf/book/signup";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "thymeleaf/book/signup";
        }

        if (memberService.isExistEmail(userCreateForm.getEmail())) {
            bindingResult.rejectValue("email", "duplicateEmail", "이미 사용된 이메일입니다.");
            return "thymeleaf/book/signup";
        }

        /* ---
        Member member = new Member();
        member.setEmail(userCreateForm.getEmail());
        member.setUsername(userCreateForm.getUsername());
        member.setPassword(userCreateForm.getPassword1());
        --- */
        Member member = Member.builder()
                .email(userCreateForm.getEmail())
                .username(userCreateForm.getUsername())
                .build();

        member.setPassword(userCreateForm.getPassword1());
        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        memberService.insertMember(member);

        return "redirect:/book/login";
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = {"/list/{code}/{sort}", "/list/{code}/{sort}/{opt}", "/list/{code}/{sort}/{opt}/{key}", "/list/{code}/{sort}/{opt}/{key}/{page}"})
    public String list(@PathVariable String code,
                       @PathVariable String sort,
                       @PathVariable(required = false) Optional<String> opt,
                       @PathVariable(required = false) Optional<String> key,
                       @PathVariable(required = false) Optional<Integer> page ,
		               @AuthenticationPrincipal MemberAdapter author,
		               Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10110");
        String name = wordMeaningService.getBookName(wordbook, code);

        List<Book> books = bookService.getBooks(wordbook, author.getMember().getId());

        String active = "N";

        for(Book book : books) {
		    if(code.equals(book.getCode())) {
                active = book.getActive();
			}
		}

        model.addAttribute("active", active);
        model.addAttribute("code", code);
        model.addAttribute("sort", sort);
        model.addAttribute("tit", name);
        model.addAttribute("opt", opt.orElse("eng"));
        model.addAttribute("key", key.orElse(""));
        model.addAttribute("page", page.orElse(0));

        return "thymeleaf/book/books";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/create", "/create/{opt}", "/create/{opt}/{key}"})
    public String createWord2(WordbookForm wordbookForm,
                              @PathVariable(required = false) Optional<String> opt,
                              @PathVariable(required = false) Optional<String> key,
                              Model model) {
        List<Open> opens = new ArrayList<>();
        Open open1 = Open.builder().id(1).name("Open").val(-1).build();
        Open open2 = Open.builder().id(2).name("Closed").val(0).build();

        opens.add(open1);
        opens.add(open2);

        model.addAttribute("opt", opt.orElse(""));
        model.addAttribute("key", key.orElse(""));
        model.addAttribute("opens", opens);

        return "thymeleaf/book/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping({"/create", "/create/{opt}", "/create/{opt}/{key}"})
    public String createWordbook(@Valid WordbookForm wordbookForm, BindingResult bindingResult,
                                 @PathVariable(required = false) Optional<String> opt,
                                 @PathVariable(required = false) Optional<String> key,
                                 Principal principal, Model model) {

        if(bindingResult.hasErrors()) {
            List<Open> opens = new ArrayList<>();
            Open open1 = Open.builder().id(1).name("공개").val(-1).build();
            Open open2 = Open.builder().id(2).name("비공개").val(0).build();

            opens.add(open1);
            opens.add(open2);
            model.addAttribute("opens", opens);
            model.addAttribute("opt", opt.orElse(""));
            model.addAttribute("key", key.orElse(""));
            return "thymeleaf/book/create";
        }

        String loc = "/book/";

        if(opt.isPresent()) {
            loc += "my/" + opt.get();
        }

        if(key.isPresent()) {
            loc += "/" + URLEncoder.encode(key.get(), StandardCharsets.UTF_8);
        }

        Member member = memberService.getMember(principal.getName());
        wordService.create(wordbookForm, member);

        return "redirect:" + loc;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public String order(Model model) {
        model.addAttribute("client_id", paypalConfig.getClientId() + "&currency=USD");
        return "thymeleaf/book/order";
    }

    @GetMapping("/success")
    public String success(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10040");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/book/intro";
    }

}
