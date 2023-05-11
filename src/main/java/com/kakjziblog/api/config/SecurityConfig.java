package com.kakjziblog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
		return http.authorizeHttpRequests()
				   .requestMatchers("/auth/login")
				   .permitAll()
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
				   .userDetailsService(userDetailsService())
				   .csrf(AbstractHttpConfigurer::disable)
				   .build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		UserDetails user = User.withUsername("jiwoo")
							   .password("1234")
							   .roles("ADMIN")
							   .build();
		manager.createUser(user);
		return manager;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}