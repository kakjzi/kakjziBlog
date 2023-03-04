package com.kakjziblog.api.controller;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakjziblog.api.config.AppConfig;
import com.kakjziblog.api.request.Login;
import com.kakjziblog.api.request.Signup;
import com.kakjziblog.api.response.SessionResponse;
import com.kakjziblog.api.service.AuthService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final AppConfig appConfig;

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody Login login) {
		log.info(">> login {}", login);
		Long userId = authService.signIn(login);

		SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

		String jws = Jwts.builder()
						 .setSubject(String.valueOf(userId))
						 .signWith(secretKey)
						 .setIssuedAt(new Date())
						 .compact();

		return new SessionResponse(jws);
	}

	@PostMapping("/auth/signup")
	public void signup(@RequestBody Signup signup) {
		authService.signup(signup);
	}
}

