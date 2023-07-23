package com.kakjziblog.api.config;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserPrincipal extends User {
	private final Long userId;
	public UserPrincipal(com.kakjziblog.api.domain.Users user) {
		super(user.getEmail(), user.getPassword(),
			List.of(
				new SimpleGrantedAuthority("ROLE_USER")
			));
		this.userId = user.getId();
	}

	public Long getUserId() {
		return userId;
	}
}
