package com.kakjziblog.api.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kakjziblog.api.domain.common.CommonEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 외부에서 접근하는 것을 막아두어 안전성을 보장합니다.
@EntityListeners(AuditingEntityListener.class) // Entity를 DB에 적용하기 이전, 이후에 커스텀 콜백을 요청할 수 있는 어노테이션
public class Post extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;
    private Long commentId;

    @Builder
    public Post(String title, String content, User user, Category category, Long commentId) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
        this.commentId = commentId;
    }

    public PostEditor.PostEditorBuilder toEidtor(){
        return PostEditor.builder()
                         .title(title)
                         .content(content)
                         .category(category);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
        this.category = postEditor.getCategory();
    }
}
