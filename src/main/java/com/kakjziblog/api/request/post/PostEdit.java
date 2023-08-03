package com.kakjziblog.api.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.kakjziblog.api.domain.Category;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostEdit {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "유효한 카테고리를 입력해주세요.")
    private Category category;

    @Builder
    public PostEdit(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
