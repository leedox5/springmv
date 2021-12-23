package kr.leedox.demo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
	public List<String> home() {
		return List.of("Hello", "SpringBoot", "Maven");
	}

    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot and Maven";
    }
}
