package com.kakjziblog.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// 	registry.addInterceptor(new AuthInterceptor())
	// 		.addPathPatterns("/api/**")
	// 		.excludePathPatterns("/error", "favicon.ico");
	// }
	private final AppConfig appConfig;

}
