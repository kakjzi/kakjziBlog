package com.kakjziblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kakjziblog.api.config.AppConfig;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class KakjziblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakjziblogApplication.class, args);
    }

}
