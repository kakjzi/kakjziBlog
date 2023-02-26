package com.kakjziblog.api.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kakjziblog.api.exception.Unauthorized;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		log.info(">> preHandle");

		String accessToken = request.getParameter("accessToken");
		if (accessToken != null && accessToken.equals("jiwoo")) {
			return true;
		} else {
			// response.sendRedirect("/api/user/login");
			throw new Unauthorized();
		}
	}
}