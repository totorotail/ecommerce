package com.zerobase.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
@ServletComponentScan
public class UserApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

}
