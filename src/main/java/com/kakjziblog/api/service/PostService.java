package com.kakjziblog.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.domain.PostEditor;
import com.kakjziblog.api.exception.PostNotFound;
import com.kakjziblog.api.exception.UserNotFound;
import com.kakjziblog.api.repository.PostRepository;
import com.kakjziblog.api.repository.UserRepository;
import com.kakjziblog.api.request.PostCreate;
import com.kakjziblog.api.request.PostEdit;
import com.kakjziblog.api.request.PostSearch;
import com.kakjziblog.api.response.PostResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void write(Long userId, PostCreate postCreate) {
        var user = userRepository.findById(userId)
                                    .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                        .user(user)
                        .title(postCreate.getTitle())
                        .content(postCreate.getContent())
                        .category(postCreate.getCategory())
                        .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        //응답클래스
        return new PostResponse(post);

    }


    // 글이 너무 많아지면 비용이 너무 많이 든다.
    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEidtor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                                                 .content(postEdit.getContent())
                                                 .category(postEdit.getCategory())
                                                 .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
