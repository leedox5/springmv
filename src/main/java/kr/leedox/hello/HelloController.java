package kr.leedox.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot and Maven!!!";
    }

    @GetMapping("/home")
    public List<String> home() {
        return List.of("Hello", "SpringBoot", "Maven");
    }
}
