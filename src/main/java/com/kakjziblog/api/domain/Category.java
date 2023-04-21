package com.kakjziblog.api.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

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
	@JsonCreator
	public static Category from(String s) {
		return Arrays.stream(Category.values())
					 .filter(category -> category.getName().equals(s))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("Invalid category name: " + s));
	}
}
