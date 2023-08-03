package com.kakjziblog.api.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakjziblog.api.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
