package kr.leedox.controller;

import kr.leedox.entity.Game;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

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
        return "WordbookList";
    }

    @PostMapping("/wordbook")
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

    @GetMapping("/wordbook/{id}")
    public String getWordbook1(@PathVariable Integer id, Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);
        return "WordbookDetail";
    }


    @GetMapping("/wordbook/{id}/{key}")
    public String getWordbook(@PathVariable Integer id, @PathVariable String key, Model model) {
        Wordbook wordbook = wordService.getWordbook(id);
        model.addAttribute("wordbook", wordbook);
        model.addAttribute("key", key);
        return "WordbookDetail";
    }

    @PostMapping("/wordbook/save/{id}")
    public String saveWordbook(@PathVariable Integer id, Model model, Wordbook wordbookForm) {
        Wordbook wordbookRepo = wordService.getWordbook(id);
        wordbookRepo.setMeaning1(wordbookForm.getMeaning1());
        wordbookRepo = wordService.saveWordbook(wordbookRepo);

        /*
        WordMeaning wordMeaning = WordMeaning.builder().wordbook(wordbookRepo).meaning(wordbookRepo.getMeaning1()).build();
        wordMeaningService.save(wordbookRepo, wordMeaning);
        */

        model.addAttribute("wordbook", wordbookRepo);
        if(!wordbookForm.getWord().isEmpty()) {
            model.addAttribute("key", wordbookForm.getWord());
        }
        return "WordbookDetail";
    }

    @PostMapping("/wordbook/savemeaning/{id}")
    public String saveWordMeaning(@PathVariable Integer id, Model model, WordMeaning wordMeaningForm) {
        Wordbook wordbookRepo = wordService.getWordbook(id);
        wordMeaningService.save(wordbookRepo, wordMeaningForm);

        model.addAttribute("wordbook", wordbookRepo);
        model.addAttribute("key", wordMeaningForm.getKey());
        return "WordbookDetail";
    }

    @GetMapping("/wordbook/deletemeaning/{id}/{key}")
    public String deleteWordMeaning(@PathVariable Integer id, @PathVariable String key, Model model) {
        WordMeaning wordMeaning = wordMeaningService.getWordbook(id);
        wordMeaningService.delete(wordMeaning);
        model.addAttribute("wordbook", wordMeaning.getWordbook());
        model.addAttribute("key", key);
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
