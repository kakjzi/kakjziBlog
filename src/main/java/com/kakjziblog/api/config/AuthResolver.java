package com.kakjziblog.api.config;

import org.apache.tomcat.util.codec.binary.Base64;
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

	private static final String KEY = "FcvuSVEJz2Iyop2Jv0IcWAfcwYDQCTf7amAqxHgyAHg=";
	private final SessionRepository sessionRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType()
						.equals(UserSession.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		String jws = webRequest.getHeader("Authorization");

		if (jws == null || jws.equals("")) {
			throw new Unauthorized();
		}
		byte[] decodeKey = Base64.decodeBase64(KEY);

		try {

			Jws<Claims> claimsJws = Jwts.parserBuilder()
										.setSigningKey(decodeKey)
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
