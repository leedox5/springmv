package kr.leedox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class LeedoxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeedoxApplication.class, args);
    }

}
