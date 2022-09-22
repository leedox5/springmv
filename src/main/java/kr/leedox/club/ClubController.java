package kr.leedox.club;

import kr.leedox.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClubController {
    @GetMapping("/record")
    public String getRecord(Model model) {
        Member member = new Member();
        member.setUsername("선수1");
        model.addAttribute("member", member);
        return "thymeleaf/club/record";
    }
}
