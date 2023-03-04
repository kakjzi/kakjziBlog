package com.kakjziblog.api.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kakjziblog.api.crypto.PasswordEncoder;
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
		Users user = userRepository.findByEmail(login.getEmail())
									 .orElseThrow(InvalidSigninInformation::new);

		PasswordEncoder passwordEncoder = new PasswordEncoder();

		boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
		if(!matches) {
			throw new InvalidSigninInformation();
		}

		return user.getId();
	}

	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(user -> {
				throw new AlreadyExistsEmailException();
			});

		PasswordEncoder encoder = new PasswordEncoder();
		String encryptedPassword = encoder.encrypt(signup.getPassword());

		Users users = Users.builder()
						   .email(signup.getEmail())
						   .name(signup.getName())
						   .password(encryptedPassword)
						   .build();

		userRepository.save(users);
	}
}
