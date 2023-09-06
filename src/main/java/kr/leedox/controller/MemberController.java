package kr.leedox.controller;

import kr.leedox.entity.Member;
import kr.leedox.entity.UserCreateForm;
import kr.leedox.entity.Wordbook;
import kr.leedox.repository.MemberRepository;
import kr.leedox.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/login")
    public String login() {
        return "thymeleaf/login";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "thymeleaf/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "thymeleaf/signup";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "thymeleaf/signup";
        }
        
		if (memberService.isExistEmail(userCreateForm.getEmail())) {
			bindingResult.rejectValue("email", "duplicateEmail", "이미 사용된 이메일입니다.");
			return "thymeleaf/signup";
		}

        /* ---
        Member member = new Member();
        member.setEmail(userCreateForm.getEmail());
        member.setUsername(userCreateForm.getUsername());
        member.setPassword(userCreateForm.getPassword1());
        --- */
        Member member = Member.builder()
              .email(userCreateForm.getUsername())
              .username(userCreateForm.getUsername())
              .build();

        member.setPassword(userCreateForm.getPassword1());
        member.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        memberService.insertMember(member);

        return "redirect:/welcome";
    }

    @GetMapping("/demo1")
    public String hello() {
        return "thymeleaf/demo1";
    }
}
