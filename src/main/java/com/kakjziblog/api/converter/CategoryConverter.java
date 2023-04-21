package com.kakjziblog.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kakjziblog.api.domain.Category;

@Component
public class CategoryConverter implements Converter<String, Category> {
	@Override
	public Category convert(String source) {
		return Category.valueOf(source);
	}
}
