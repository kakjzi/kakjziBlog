package com.kakjziblog.api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakjziblog.api.config.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MainController {

	@GetMapping("/")
	public String main() {
		return "메인 페이지입니다.";
	}
	@GetMapping("/user")
	public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		Long userId = userPrincipal.getUserId();

		log.info("userId: {}", userId);
		return "유저 페이지입니다.🥰";
	}
	@GetMapping("/admin")
	public String admin() {
		return "관리자 페이지입니다.😅";
	}
}
