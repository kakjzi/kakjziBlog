package com.kakjziblog.api.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.config.filter.EmailPasswordAuthFilter;
import com.kakjziblog.api.config.handler.Http401Handler;
import com.kakjziblog.api.config.handler.Http403Handler;
import com.kakjziblog.api.config.handler.LoginFailHandler;
import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring 6 부터는 Bean 주입 방식으로 변경됨
 */
@Configuration
@EnableWebSecurity(debug = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

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
				   .requestMatchers("/auth/signup")
				   .permitAll()
				   .requestMatchers("/user")
				   .hasAnyRole("USER", "ADMIN")
				   .requestMatchers("/admin")
				   .access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasAuthority('WRITE')"))
				   .anyRequest()
				   .authenticated()
				   .and()
				   .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
				   .exceptionHandling(e -> {
					   e.accessDeniedHandler(new Http403Handler(objectMapper));
					   e.authenticationEntryPoint(new Http401Handler(objectMapper));
				   })
				   .rememberMe(rm -> rm.rememberMeParameter("remember")
									   .alwaysRemember(false)
									   .tokenValiditySeconds(2592000))
				   .csrf(AbstractHttpConfigurer::disable)
				   .build();
	}

	@Bean
	public EmailPasswordAuthFilter emailPasswordAuthFilter(){
		EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login", objectMapper);
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
		filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
		filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

		SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
		rememberMeServices.setAlwaysRemember(true);
		rememberMeServices.setValiditySeconds(3600 * 24 * 30);
		filter.setRememberMeServices(rememberMeServices);
		return filter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(provider);
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