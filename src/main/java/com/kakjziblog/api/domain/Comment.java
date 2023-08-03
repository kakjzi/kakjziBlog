package com.kakjziblog.api.domain;

import static jakarta.persistence.GenerationType.*;

import com.kakjziblog.api.domain.common.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	indexes = {
		@Index(name = "idx_comment_post_id", columnList = "post_id"),
	}
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Comment extends CommonEntity {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn
	private Post post;

	@Builder
	public Comment(String author, String password, String content) {
		this.author = author;
		this.password = password;
		this.content = content;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
