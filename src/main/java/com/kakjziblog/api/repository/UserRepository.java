package com.kakjziblog.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakjziblog.api.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findByEmail(String email);
}
