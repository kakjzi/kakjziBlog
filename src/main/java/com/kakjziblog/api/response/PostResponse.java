package com.kakjziblog.api.response;

import java.time.LocalDateTime;

import com.kakjziblog.api.domain.Post;

import lombok.Getter;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final LocalDateTime lastUpdateDate;

    //생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().getName();
        this.lastUpdateDate = post.getUpdatedAt();
    }
}
