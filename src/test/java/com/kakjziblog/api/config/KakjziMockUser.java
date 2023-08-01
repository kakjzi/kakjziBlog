package com.kakjziblog.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = KakjziMockSecurityContext.class)
public @interface KakjziMockUser {

	String name() default "신지우";

	String email() default "jiwoo.sin@naver.com";

	String password() default "1234";

	String role() default "ROLE_ADMIN";
}
