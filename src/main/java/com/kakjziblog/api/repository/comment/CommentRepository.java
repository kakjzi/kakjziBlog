package com.kakjziblog.api.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kakjziblog.api.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
