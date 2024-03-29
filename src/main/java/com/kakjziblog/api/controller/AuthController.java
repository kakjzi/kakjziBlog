package com.kakjziblog.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakjziblog.api.config.AppConfig;
import com.kakjziblog.api.request.Signup;
import com.kakjziblog.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AppConfig appConfig;

	@PostMapping("/auth/signup")
	public void signup(@RequestBody Signup signup) {
		authService.signup(signup);
	}
}

