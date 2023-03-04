package com.kakjziblog.api.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.exception.AlreadyExistsEmailException;
import com.kakjziblog.api.repository.UserRepository;
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
	void test1() throws Exception {
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
		assertThat(user.getPassword()).isEqualTo("1234");
	}

	@Test
	@DisplayName("중복 이메일")
	void test2() throws Exception {
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
}