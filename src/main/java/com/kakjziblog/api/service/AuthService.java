package com.kakjziblog.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.exception.AlreadyExistsEmailException;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(user -> {
				throw new AlreadyExistsEmailException();
			});

		String encryptedPassword = passwordEncoder.encode(signup.getPassword());

		User user = User.builder()
						   .email(signup.getEmail())
						   .name(signup.getName())
						   .password(encryptedPassword)
						   .build();

		userRepository.save(user);
	}
}
