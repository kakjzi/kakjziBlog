package com.kakjziblog.api.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.kakjziblog.api.config.data.UserSession;
import com.kakjziblog.api.exception.Unauthorized;
import com.kakjziblog.api.repository.SessionRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

	private final SessionRepository sessionRepository;
	private final AppConfig appConfig;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType()
						.equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		log.info(">> {}", appConfig.toString());

		String jws = webRequest.getHeader("Authorization");

		if (jws == null || jws.equals("")) {
			throw new Unauthorized();
		}

		try {

			Jws<Claims> claimsJws = Jwts.parserBuilder()
										.setSigningKey(appConfig.getJwtKey())
										.build()
										.parseClaimsJws(jws);

			String userId = claimsJws.getBody()
									 .getSubject();

			return new UserSession(Long.valueOf(userId));
		} catch (JwtException e) {
			throw new Unauthorized();
		}
	}
}
