package kr.leedox.admin;

import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    WordService wordService;

    @GetMapping("/wordbook")
    public String wordbook(Model model) {
        List<Wordbook> wordbooks = wordService.getListAll();
    }
}
