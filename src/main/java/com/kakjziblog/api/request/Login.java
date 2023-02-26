package com.kakjziblog.api.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Login {
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	public Login() {
	}

	@Builder
	public Login(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
