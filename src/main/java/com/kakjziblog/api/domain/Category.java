package com.kakjziblog.api.domain;

public enum Category {
	DEVELOP("개발"),
	LIFE("일상"),
	TRAVEL("여행");

	private final String name;

	Category(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
