package com.kakjziblog.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Signup {
	private String email;
	private String name;
	private String password;

	public Signup() {
	}

	@Builder
	public Signup(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
}
