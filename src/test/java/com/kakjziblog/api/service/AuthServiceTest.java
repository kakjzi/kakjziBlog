package com.kakjziblog.api.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kakjziblog.api.domain.User;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Signup;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

	@Autowired private UserRepository userRepository;

	@Autowired private AuthService authService;
	// @Autowired private PasswordEncoder encoder;

	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void test1() {
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

		User user = userRepository.findAll()
								  .iterator()
								  .next();

		assertThat(user.getEmail()).isEqualTo("jiwoo@naver.com");
		assertThat(user.getName()).isEqualTo("jiwoo");
	}
}