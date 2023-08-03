package com.kakjziblog.api.request.comment;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CommentCreate {

	@Length(min = 1, max = 8, message = "작성자는 1자 이상 8자 이하로 입력해주세요.")
	@NotBlank(message = "작성자를 입력해주세요.")
	private String author;

	@Length(min = 6, max = 30, message = "작성자는 6자 이상 30자 이하로 입력해주세요.")
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	@Length(min = 10, max = 1000, message = "작성자는 10자 이상 1000자 이하로 입력해주세요.")
	@NotBlank(message = "내용을 입력해주세요.")
	private String content;

	@Builder
	public CommentCreate(String author, String password, String content) {
		this.author = author;
		this.password = password;
		this.content = content;
	}
}
