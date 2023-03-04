package com.kakjziblog.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakjziblog.api.domain.Session;
import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.exception.AlreadyExistsEmailException;
import com.kakjziblog.api.exception.InvalidSigninInformation;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Login;
import com.kakjziblog.api.request.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;

	@Transactional
	public Long signIn(Login login) {
		Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(InvalidSigninInformation::new);

		Session session = users.addSession();
		return users.getId();
	}

	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(user -> {
				throw new AlreadyExistsEmailException();
			});

		Users users = Users.builder()
			.email(signup.getEmail())
			.name(signup.getName())
			.password(signup.getPassword())
			.build();

		userRepository.save(users);
	}
}
