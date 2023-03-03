package com.kakjziblog.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jiwoo")
public class AppConfig {
	public String key;
}
