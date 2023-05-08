package com.kakjziblog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring 6 부터는 Bean 주입 방식으로 변경됨
 */
@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
				 .requestMatchers("/favicon.ico", "/error")
				 .requestMatchers(toH2Console());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.authorizeHttpRequests()
				   .requestMatchers("/auth/login").permitAll()
				   .anyRequest().authenticated()
			.and()
			.csrf(AbstractHttpConfigurer::disable)
			.build();
	}
}