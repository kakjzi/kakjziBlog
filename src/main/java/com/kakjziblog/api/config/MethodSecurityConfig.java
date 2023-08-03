package com.kakjziblog.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.kakjziblog.api.repository.post.PostRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class MethodSecurityConfig {
	private final PostRepository postRepository;
	@Bean
	public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
		var handler = new DefaultMethodSecurityExpressionHandler();
		handler.setPermissionEvaluator(new KakjziPermissionEvaluator(postRepository));
		return handler;
	}
}
