package com.kakjziblog.api.domain;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accessToken;

	@ManyToOne
	private Users user;

	@Builder
	public Session(Users user) {
		this.accessToken = UUID.randomUUID().toString();
		this.user = user;
	}
}
