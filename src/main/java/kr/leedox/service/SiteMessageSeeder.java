package kr.leedox.service;

import kr.leedox.entity.SiteMessage;
import kr.leedox.repository.SiteMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SiteMessageSeeder implements ApplicationRunner {
    private final SiteMessageRepository siteMessageRepository;
    private final MessageService messageService;

    @Override
    public void run(ApplicationArguments args) {
        seed("menu.intro", "ko", "소개");
        seed("menu.intro", "en", "Intro");
        seed("menu.stat", "ko", "통계");
        seed("menu.stat", "en", "Statistics");
        seed("menu.list", "ko", "목록");
        seed("menu.list", "en", "List");
        seed("menu.login", "ko", "로그인");
        seed("menu.login", "en", "Login");
        seed("menu.logout", "ko", "로그아웃");
        seed("menu.logout", "en", "Logout");

        seed("signin.0010", "ko", "로그인");
        seed("signin.0010", "en", "Sign in");
        seed("signin.0020", "ko", "계정이 없으신가요?");
        seed("signin.0020", "en", "Need an account?");
        seed("signin.0030", "ko", "회원가입");
        seed("signin.0030", "en", "Sign up");
        seed("signin.0040", "ko", "이메일");
        seed("signin.0040", "en", "Email");
        seed("signin.0050", "ko", "비밀번호");
        seed("signin.0050", "en", "Password");
        seed("signin.0060", "ko", "로그인 유지");
        seed("signin.0060", "en", "Remember me");

        seed("signup.title", "ko", "회원가입");
        seed("signup.title", "en", "Sign up");
        seed("signup.email", "ko", "이메일");
        seed("signup.email", "en", "Email");
        seed("signup.name", "ko", "이름");
        seed("signup.name", "en", "Name");
        seed("signup.pass1", "ko", "비밀번호");
        seed("signup.pass1", "en", "Password");
        seed("signup.pass2", "ko", "비밀번호 확인");
        seed("signup.pass2", "en", "Confirm password");
        seed("signup.join", "ko", "가입");
        seed("signup.join", "en", "Join");

        messageService.clearCache();
    }

    private void seed(String code, String locale, String content) {
        siteMessageRepository.findByCodeAndLocale(code, locale)
                .orElseGet(() -> siteMessageRepository.save(SiteMessage.builder()
                        .code(code)
                        .locale(locale)
                        .content(content)
                        .build()));
    }
}
