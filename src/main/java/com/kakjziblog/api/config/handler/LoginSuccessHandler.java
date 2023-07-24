package com.kakjziblog.api.config.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.config.UserPrincipal;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		log.info("[인증성공] user={}", userPrincipal.getUsername());

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8.displayName());
		response.setStatus(SC_OK);
		String username = userPrincipal.getUsername();
		Collection<GrantedAuthority> authorities = userPrincipal.getAuthorities();
		objectMapper.writeValue(response.getWriter(), Map.of("username", username, "authorities", authorities));
	}
}
