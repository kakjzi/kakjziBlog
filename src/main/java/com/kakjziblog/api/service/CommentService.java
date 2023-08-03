package com.kakjziblog.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakjziblog.api.domain.Comment;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.exception.CommentNotFound;
import com.kakjziblog.api.exception.InvalidPassword;
import com.kakjziblog.api.repository.comment.CommentRepository;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.request.comment.CommentCreate;
import com.kakjziblog.api.request.comment.CommentDelete;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;


	@Transactional
	public void write(Long postId, CommentCreate request) {
		Post post = postRepository.findById(postId)
								  .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

		String encodedPassword = passwordEncoder.encode(request.getPassword());

		Comment comment = Comment.builder()
								 .author(request.getAuthor())
								 .password(encodedPassword)
								 .content(request.getContent())
								 .build();

		post.addComment(comment);
	}

	public void delete(Long commentId, CommentDelete request) {
		Comment comment = commentRepository.findById(commentId)
										   .orElseThrow(CommentNotFound::new);

		String encodedPassword = comment.getPassword();
		if (!passwordEncoder.matches(request.getPassword(), encodedPassword)) {
			throw new InvalidPassword();
		}

		commentRepository.delete(comment);
	}
}
