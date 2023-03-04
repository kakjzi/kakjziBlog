package com.kakjziblog.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.domain.Session;
import com.kakjziblog.api.domain.Users;
import com.kakjziblog.api.repository.SessionRepository;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Login;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;
	@BeforeEach
	void clean(){
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("로그인 성공")
	void test1() throws Exception {
		//given
		userRepository.save(Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build());

		Login login = Login.builder()
			.email("jiwoo99@test.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andDo(print());
	}
	@Test
	@Transactional
	@DisplayName("로그인 성공 후 세션 1개 생성")
	void test2() throws Exception {
		//given
		Users user = userRepository.save(Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build());

		Login login = Login.builder()
			.email("jiwoo99@test.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andDo(print());

		Users loginUser = userRepository.findById(user.getId())
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));

		assertThat(loginUser.getSessions().size()).isEqualTo(1);
	}
	@Test
	@Transactional
	@DisplayName("로그인 성공 후 세션 응답")
	void test3() throws Exception {
		//given
		Users user = userRepository.save(Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build());

		Login login = Login.builder()
			.email("jiwoo99@test.com")
			.password("1234")
			.build();

		String json = objectMapper.writeValueAsString(login);

		mockMvc.perform(post("/auth/login")
				.contentType(APPLICATION_JSON)
				.content(json)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").exists())
			.andDo(print());
	}
	@Test
	@DisplayName("로그인 성공 후 권한이 필요한 페이지 접속한다 /foo")
	void test4() throws Exception {

		Users user = Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build();

		Session session = user.addSession();
		userRepository.save(user);

		mockMvc.perform(get("/foo")
				.header("Authorization", session.getAccessToken())
				.contentType(APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}
	@Test
	@DisplayName("검증되지 않은 세션으로 권한이 필요한 페이지에 접속할 수 없음 /foo")
	void test5() throws Exception {

		Users user = Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build();

		Session session = user.addSession();
		userRepository.save(user);

		mockMvc.perform(get("/foo")
				.header("Authorization", session.getAccessToken() + "error!!")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}
	@Test
	@DisplayName("회원가입")
	void test6() throws Exception {

		Users user = Users.builder()
			.name("jiwoo")
			.password("1234")
			.email("jiwoo99@test.com")
			.build();

		Session session = user.addSession();
		userRepository.save(user);

		mockMvc.perform(get("/foo")
				.header("Authorization", session.getAccessToken() + "error!!")
				.contentType(APPLICATION_JSON))
			.andExpect(status().isUnauthorized())
			.andDo(print());
	}
}