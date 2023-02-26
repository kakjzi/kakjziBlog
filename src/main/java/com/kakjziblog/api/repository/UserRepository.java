package com.kakjziblog.api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.kakjziblog.api.domain.Users;

public interface UserRepository extends CrudRepository<Users, Long> {

	Optional<Users> findByEmailAndPassword(String email, String password);
}
