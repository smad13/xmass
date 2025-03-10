package com.xmas.greet.seguridad;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class GreetApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreetApplication.class, args);
    }
}
