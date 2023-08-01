package com.kakjziblog.api.config;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakjziMockSecurityContext implements WithSecurityContextFactory<KakjziMockUser> {

	private final UserRepository userRepository;
	@Override
	public SecurityContext createSecurityContext(KakjziMockUser annotation) {
		var user = User.builder()
					   .name(annotation.name())
					   .email(annotation.email())
					   .password(annotation.password())
					   .build();

		userRepository.save(user);

		var principal = new UserPrincipal(user);
		var role = new SimpleGrantedAuthority("ROLE_ADMIN");
		var auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), List.of(role));
		var context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(auth);

		return context;
	}
}
