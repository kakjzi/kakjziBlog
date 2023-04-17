package com.kakjziblog.api.response;

import com.kakjziblog.api.domain.Category;
import com.kakjziblog.api.domain.Post;
import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;

    //생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
    }

    @Builder
    public PostResponse(Long id, String title, String content, Category category) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
        this.category = category;
    }
}
