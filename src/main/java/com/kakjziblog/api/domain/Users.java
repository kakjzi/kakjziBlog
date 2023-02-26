package com.kakjziblog.api.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Session> sessions = new ArrayList<>();

	public Session addSession() {
		Session session =Session.builder()
			.user(this)
			.build();

		sessions.add(session);
		return session;
	}

	@Builder
	public Users(String name, String email, String password, LocalDateTime createdAt) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.createdAt = LocalDateTime.now();
	}
}
