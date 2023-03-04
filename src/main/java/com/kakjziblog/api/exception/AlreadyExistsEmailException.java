package com.kakjziblog.api.exception;

public class AlreadyExistsEmailException extends KakjziblogException {
	private static final String MESSAGE = "이미 존재하는 이메일입니다.";

	public AlreadyExistsEmailException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 400;
	}
}
