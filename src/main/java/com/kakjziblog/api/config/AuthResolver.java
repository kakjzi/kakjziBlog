package com.kakjziblog.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.kakjziblog.api.config.data.UserSession;
import com.kakjziblog.api.exception.Unauthorized;

public class AuthResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		String accessToken = webRequest.getHeader("accessToken");
		if(accessToken == null || !accessToken.equals("")) {
			throw new Unauthorized();
		}


		return new UserSession(1L);
	}
}
