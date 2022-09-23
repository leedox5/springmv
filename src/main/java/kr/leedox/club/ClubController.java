package kr.leedox.club;

import kr.leedox.entity.Game;
import kr.leedox.entity.Member;
import kr.leedox.entity.UserCreateForm;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.GameService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/club")
public class ClubController {

    @Autowired
    GameService gameService;

    @Autowired
    WordService wordService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/record")
    public String getRecord(Model model) {
        Member member = new Member();
        member.setUsername("선수1");
        model.addAttribute("member", member);
        return "thymeleaf/club/record";
    }

    @GetMapping("/meeting")
    public String getMeeting(Model model) {
        List<Game> gameList = gameService.getList();

        model.addAttribute("list", gameList);
        return "thymeleaf/club/meeting";
    }

    @GetMapping("/intro")
    public String getIntro(Model model) {
        Wordbook wordbook = wordService.getWordbookByWord("10050");
        model.addAttribute("wordbook", wordbook);
        return "thymeleaf/club/intro";
    }

    @RequestMapping("/login")
    public String login() {
        return "thymeleaf/club/login";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "thymeleaf/club/signup";
    }

}
