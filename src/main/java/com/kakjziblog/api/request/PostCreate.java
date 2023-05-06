package com.kakjziblog.api.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.kakjziblog.api.domain.Category;
import com.kakjziblog.api.exception.InvalidRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "유효한 카테고리를 입력해주세요.")
    private Category category;

    public PostCreate() {
    }
    @Builder
    public PostCreate(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void validate() {
        if(title.contains("바보")){
            throw new InvalidRequest("title", "제목에 '바보'는 포함할 수 없습니다.");
        }
    }
}