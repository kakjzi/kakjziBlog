package com.kakjziblog.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakjziblog.api.request.Login;
import com.kakjziblog.api.response.SessionResponse;
import com.kakjziblog.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody Login login) {
		log.info(">> login {}", login);

		return new SessionResponse(authService.signIn(login));
	}
}
