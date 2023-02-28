package com.kakjziblog.api.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.kakjziblog.api.config.data.UserSession;
import com.kakjziblog.api.domain.Session;
import com.kakjziblog.api.exception.Unauthorized;
import com.kakjziblog.api.repository.SessionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

	private final SessionRepository sessionRepository;
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) throws Exception {

		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

		if(servletRequest == null) {
			throw new Unauthorized();
		}

		Cookie[] cookies = servletRequest.getCookies();
		if (cookies.length == 0) {
			throw new Unauthorized();
		}

		String accessToken = cookies[0].getValue();
		Session session = sessionRepository.findByAccessToken(accessToken)
			.orElseThrow(Unauthorized::new);

		return new UserSession(session.getUser().getId());
	}
}
