package com.kakjziblog.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kakjziblog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
	Optional<Session> findByAccessToken(String accessToken);
}
