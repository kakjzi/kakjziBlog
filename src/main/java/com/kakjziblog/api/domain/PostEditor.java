package com.kakjziblog.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String content;
    private final Category category;

    @Builder
    public PostEditor(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
