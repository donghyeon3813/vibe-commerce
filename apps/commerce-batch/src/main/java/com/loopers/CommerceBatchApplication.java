package com.loopers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.loopers.repository")
public class CommerceBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommerceBatchApplication.class, args);
    }

}
