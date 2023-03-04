package com.kakjziblog.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakjziblog.api.crypto.PasswordEncoder;
import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.exception.AlreadyExistsEmailException;
import com.kakjziblog.api.exception.InvalidSigninInformation;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Login;
import com.kakjziblog.api.request.Signup;

@SpringBootTest
class AuthServiceTest {

	@Autowired private UserRepository userRepository;

	@Autowired private AuthService authService;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void test1() {
		PasswordEncoder encoder = new PasswordEncoder();
		//given
		Signup signup = Signup.builder()
							  .name("jiwoo")
							  .password("1234")
							  .email("jiwoo@naver.com")
							  .build();
		//when
		authService.signup(signup);
		//then
		assertThat(userRepository.count()).isEqualTo(1);

		Users user = userRepository.findAll()
								   .iterator()
								   .next();

		assertThat(user.getEmail()).isEqualTo("jiwoo@naver.com");
		assertThat(user.getName()).isEqualTo("jiwoo");
		assertTrue(encoder.matches("1234", user.getPassword()));
	}

	@Test
	@DisplayName("중복 이메일")
	void test2() {
		//given
		Users alreadyUser = Users.builder()
								 .name("jiwoo")
								 .password("1234")
								 .email("jiwoo00@naver.com")
								 .build();
		userRepository.save(alreadyUser);

		Signup signup = Signup.builder()
							  .name("jiwoo")
							  .password("1234")
							  .email("jiwoo00@naver.com")
							  .build();
		//when
		assertThatThrownBy(() -> authService.signup(signup)).isInstanceOf(AlreadyExistsEmailException.class)
															.hasMessageContaining("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName("로그인 성공")
	void test3() {
		//given
		PasswordEncoder encoder = new PasswordEncoder();
		String encryptedPassword = encoder.encrypt("1234");

		Users user = Users.builder()
						  .name("jiwoo")
						  .password(encryptedPassword)
						  .email("jiwoo00@naver.com")
						  .build();
		userRepository.save(user);

		Login login = Login.builder()
						   .email("jiwoo00@naver.com")
						   .password("1234")
						   .build();
		//when
		Long userId = authService.signIn(login);
		//then
		assertNotNull(userId);
	}

	@Test
	@DisplayName("비번 틀림")
	void test4() {
		//given
		PasswordEncoder encoder = new PasswordEncoder();
		String encryptedPassword = encoder.encrypt("1234");

		Signup signup = Signup.builder()
							  .name("jiwoo")
							  .password(encryptedPassword)
							  .email("jiwoo@naver.com")
							  .build();
		authService.signup(signup);

		Login login = Login.builder()
						   .email("jiwoo@naver.com")
						   .password("5678")
						   .build();

		assertThatThrownBy(() -> authService.signIn(login)).isInstanceOf(InvalidSigninInformation.class)
														   .hasMessageContaining("");

	}
}