package io.github.mityavasilyev.springreactbarapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringReactBarAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringReactBarAppApplication.class, args);
    }

    // TODO: 16.02.2022 Move this someplace else
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
