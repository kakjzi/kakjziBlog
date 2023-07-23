package com.kakjziblog.api.config.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Http401Handler implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		log.error("[인증오류] 로그인이 필요합니다.");

		ErrorResponse errorResponse = ErrorResponse.builder()
												   .code("401")
												   .message("[인증오류] 로그인이 필요합니다.")
												   .build();
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8.displayName());
		response.setStatus(SC_UNAUTHORIZED);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
