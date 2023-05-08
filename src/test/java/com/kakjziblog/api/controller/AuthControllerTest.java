package com.kakjziblog.api.controller;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.Signup;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void clean(){
		userRepository.deleteAll();
	}
	@Test
	@DisplayName("회원가입")
	void test1() throws Exception {

		Signup signup = Signup.builder()
							  .name("jiwoo")
							  .password("1234")
							  .email("jiwoo00@naver.com")
							  .build();

		mockMvc.perform(post("/auth/signup")
				   .content(objectMapper.writeValueAsString(signup))
				   .contentType(APPLICATION_JSON))
			   .andExpect(status().isOk())
			   .andDo(print());
	}
}