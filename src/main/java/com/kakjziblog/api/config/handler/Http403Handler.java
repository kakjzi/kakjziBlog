package com.kakjziblog.api.config.handler;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor

public class Http403Handler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {
		log.error("403[인증오류]");

		ErrorResponse errorResponse = ErrorResponse.builder()
												   .code("403")
												   .message("접근할 수 없습니다.")
												   .build();

		response.setContentType(APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(UTF_8.displayName());
		response.setStatus(SC_FORBIDDEN);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
