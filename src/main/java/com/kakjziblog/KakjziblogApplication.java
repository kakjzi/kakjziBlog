package com.kakjziblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.kakjziblog.api.config.AppConfig;

@EnableConfigurationProperties(AppConfig.class)
@EnableJpaAuditing
@SpringBootApplication
public class KakjziblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakjziblogApplication.class, args);
    }

}

