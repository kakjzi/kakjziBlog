package com.kakjziblog.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.exception.InvalidSigninInformation;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Login;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	@Transactional
	public void signIn(Login login) {
		Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(InvalidSigninInformation::new);

		users.addSession();
	}
}
