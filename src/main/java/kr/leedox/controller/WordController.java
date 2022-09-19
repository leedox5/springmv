package kr.leedox.controller;

import kr.leedox.entity.Game;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
public class WordController {

    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;

    @GetMapping("/wordbook")
    public String getList(Model model) {
        List<Wordbook> words = wordService.getList();
        model.addAttribute("list", words);
        model.addAttribute("path","");
        return "WordbookList";
    }


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

    @GetMapping("/wordbook2")
    public String getList2(Model model) {
        List<Wordbook> words = wordService.getList();
        model.addAttribute("list", words);
        model.addAttribute("path","");
        return "thymeleaf/wordbook_list";
    }

    @PostMapping("/wordpost")
    public String wordPost() {
        return "Hello, POST";
    }

    @PostMapping("/wordbook")
    public String getListOpt(Model model, @RequestParam MultiValueMap<String, String> formData) {
        List<Wordbook> words = null;
        System.out.println(formData);
        String selEng = "";
        String selKor = "";
        String selNum = "";
        String selTag = "";
        String path = "";

        if("eng".equals(formData.getFirst("opt"))) {
            selEng = "selected";
            path = "eng";
            if(formData.getFirst("key").isEmpty()) {
                words = wordService.getList();
            } else {
                words = wordService.getList(formData.getFirst("key"));
                model.addAttribute("key", formData.getFirst("key"));
                path += "/" + formData.getFirst("key");
            }
            System.out.println("ENG" + words.size());
        } else if ("kor".equals(formData.getFirst("opt"))) {
            selKor = "selected";
            path = formData.getFirst("opt");
            if (formData.getFirst("key").isEmpty()) {
                words = wordService.getList();
            } else {
                words = wordService.getListByMeaning(formData.getFirst("key"));
                model.addAttribute("key", formData.getFirst("key"));
                path += "/" + formData.getFirst("key");
            }
        } else if ("num".equals(formData.getFirst("opt"))) {
            selNum = "selected";
            path = formData.getFirst("opt");
            if (formData.getFirst("key").isEmpty()) {
                words = wordService.getList();
            } else {
                words = wordService.getListBySeq(formData.getFirst("key"));
                model.addAttribute("key", formData.getFirst("key"));
                path += "/" + formData.getFirst("key");
            }
        } else if ("tag".equals(formData.getFirst("opt"))) {
            selTag = "selected";
            path = formData.getFirst("opt");
            if (formData.getFirst("key").isEmpty()) {
                words = wordService.getList();
            } else {
                words = wordService.getListByTag(formData.getFirst("key"));
                model.addAttribute("key", formData.getFirst("key"));
                path += "/" + formData.getFirst("key");
            }
        } else {
            words = wordService.getList();
        }
        model.addAttribute("list", words);
        model.addAttribute("selEng", selEng);
        model.addAttribute("selKor", selKor);
        model.addAttribute("selNum", selNum);
        model.addAttribute("selTag", selTag);
        model.addAttribute("path", path);

        return "WordbookList";
    }

    @GetMapping("/wordbook1/{id}")
    public String getWordbook1(@PathVariable Integer id, Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);
        return "WordbookDetail";
    }


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

    @GetMapping( value = {"/wordbook2/{id}", "/wordbook2/{id}/{opt}", "/wordbook2/{id}/{opt}/{key}"})
    public String getWordbook2(@PathVariable Integer id,
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

        return "thymeleaf/detail";
    }


    @PostMapping( value = {"/wordbook/save/{id}", "/wordbook/save/{id}/{opt}", "/wordbook/save/{id}/{opt}/{key}"})
    public String saveWordbook(@PathVariable Integer id,
                               @PathVariable( required = false) Optional<String> opt,
                               @PathVariable( required = false) Optional<String> key,
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

    @PostMapping( value = {"/wordbook2/savemeaning/{id}", "/wordbook/savemeaning/{id}/{opt}", "/wordbook/savemeaning/{id}/{opt}/{key}"})
    public String saveWordMeaning2(@PathVariable Integer id,
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

        return "thymeleaf/detail";
    }

    @GetMapping(value = {"/wordbook/deletemeaning/{id}", "/wordbook/deletemeaning/{id}/{opt}", "/wordbook/deletemeaning/{id}/{opt}/{key}"})
    public String deleteWordMeaning(@PathVariable Integer id,
                                    @PathVariable(required = false) Optional<String> opt,
                                    @PathVariable(required = false) Optional<String> key,
                                    Model model) {
        WordMeaning wordMeaning = wordMeaningService.getWordbook(id);
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

    @PostMapping("/wordbook/create")
    public RedirectView createGame(Wordbook wordbook) {
        wordService.create(wordbook);
        return new RedirectView("/wordbook");
    }
}
