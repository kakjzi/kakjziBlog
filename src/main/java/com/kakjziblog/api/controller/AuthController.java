package com.kakjziblog.api.controller;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakjziblog.api.request.Login;
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
	private static final String KEY = "FcvuSVEJz2Iyop2Jv0IcWAfcwYDQCTf7amAqxHgyAHg=";

	@PostMapping("/auth/login")
	public SessionResponse login(@RequestBody Login login) {
		log.info(">> login {}", login);
		Long userId = authService.signIn(login);

		SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder()
													   .decode(KEY));

		String jws = Jwts.builder()
						 .setSubject(String.valueOf(userId))
						 .signWith(secretKey)
						 .compact();

		return new SessionResponse(jws);
	}
}
