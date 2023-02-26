package com.kakjziblog.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.kakjziblog.api.domain.Session;

public interface SessionRepository extends CrudRepository<Session, Long> {
}
