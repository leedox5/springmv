package kr.leedox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class LeedoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeedoxApplication.class, args);
    }

}
