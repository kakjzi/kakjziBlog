package com.kakjziblog.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakjziblog.api.domain.Comment;
import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.repository.comment.CommentRepository;
import com.kakjziblog.api.repository.post.PostRepository;
import com.kakjziblog.api.request.comment.CommentCreate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public void write(Long postId, CommentCreate request) {
		Post post = postRepository.findById(postId)
								  .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
		Comment comment = Comment.builder()
								 .author(request.getAuthor())
								 .password(request.getPassword())
								 .content(request.getContent())
								 .build();

		post.addComment(comment);
	}
}
