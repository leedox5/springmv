package kr.leedox.wordbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.Member;
import kr.leedox.entity.Wordbook;
import kr.leedox.controller.Open;
import kr.leedox.service.MemberService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;

@Controller
@RequestMapping("/wordbook")
public class WordbookController {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final ObjectMapper objectMapper;
	private final PaypalConfig paypalConfig;

    @Autowired
    MemberService memberService;

    @Autowired
    WordService wordService;

    public WordbookController(ObjectMapper objectMapper, PaypalConfig paypalConfig) {
        this.objectMapper = objectMapper;
		this.paypalConfig = paypalConfig;
    }

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
        return "thymeleaf/wordbook/intro";
    }

    @GetMapping("/success")
    public String success(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10040");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/wordbook/intro";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/order")
    public String order(Model model) {
		model.addAttribute("client_id", paypalConfig.getClientId() + "&currency=USD");
        return "thymeleaf/wordbook/order";
    }

    @PostMapping("/orders")
    public Object createOrder(@RequestBody OrderDTO order) throws JsonProcessingException {
        System.out.println(order);
        String orderJson = objectMapper.writeValueAsString(order);
        System.out.println(orderJson);

        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
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
