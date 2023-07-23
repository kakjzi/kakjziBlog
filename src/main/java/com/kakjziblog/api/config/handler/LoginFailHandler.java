package com.kakjziblog.api.config.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

	private final ObjectMapper objectMapper;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {
		log.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.");

		ErrorResponse errorResponse = ErrorResponse.builder()
												   .code("400")
												   .message("아이디 혹은 비밀번호가 올바르지 않습니다.")
												   .build();

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8.displayName());
		response.setStatus(SC_BAD_REQUEST);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
