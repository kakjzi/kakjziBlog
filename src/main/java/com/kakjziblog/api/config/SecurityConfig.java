package com.kakjziblog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.repository.UserRepository;

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
		return http.authorizeHttpRequests()
				   .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
				   .requestMatchers(HttpMethod.POST, "/auth/signup").permitAll()
				   .anyRequest()
				   .authenticated()
				   .and()
				   .formLogin()
				   .loginPage("/auth/login")
				   .loginProcessingUrl("/auth/login")
				   .usernameParameter("username")
				   .passwordParameter("password")
				   .defaultSuccessUrl("/")
				   .and()
				   .rememberMe(rm -> rm.rememberMeParameter("remember")
									   .alwaysRemember(false)
									   .tokenValiditySeconds(2592000))
				   .csrf(AbstractHttpConfigurer::disable)
				   .build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> {
			Users users = userRepository.findByEmail(username)
										.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
			return new UserPrincipal(users);
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new SCryptPasswordEncoder(16, 8, 1, 32, 62);
	}
}