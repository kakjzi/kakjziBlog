package com.kakjziblog.api.config;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.exception.PostNotFound;
import com.kakjziblog.api.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakjziPermissionEvaluator implements PermissionEvaluator {

	private final PostRepository postRepository;
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();

		Post post = postRepository.findById((Long)targetId)
								  .orElseThrow(PostNotFound::new);

		return true;
	}
}
