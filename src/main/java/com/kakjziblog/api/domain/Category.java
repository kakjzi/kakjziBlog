package com.kakjziblog.api.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
	DEVELOP("개발"),
	LIFE("일상"),
	TRAVEL("여행");

	private final String name;
	public String getName() {
		return name;
	}

	/**
	 * json 데이터를 파싱할 때 enum 타입을 deserialize 할 때는 기본적으로 제공하는 EnumDeserializer 를 사용한다.
	 * @JsonCreator 어노테이션으로 메소드를 생성하면 FactoryBasedEnumDeserializer 를 사용한다
	 */
	@JsonCreator
	public static Category from(String s) {
		return Arrays.stream(Category.values())
					 .filter(category -> category.getName().equals(s))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("Invalid category name: " + s));
	}
}
