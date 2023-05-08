package com.kakjziblog.api.service;

import org.springframework.stereotype.Service;

import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	public void signup(Signup signup) {
		// userRepository.findByEmail(signup.getEmail())
		// 	.ifPresent(user -> {
		// 		throw new AlreadyExistsEmailException();
		// 	});
		//
		// String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());
		//
		// Users users = Users.builder()
		// 				   .email(signup.getEmail())
		// 				   .name(signup.getName())
		// 				   .password(encryptedPassword)
		// 				   .build();
		//
		// userRepository.save(users);
	}
}
