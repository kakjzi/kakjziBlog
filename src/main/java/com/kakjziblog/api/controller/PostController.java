package com.kakjziblog.api.controller;

import com.kakjziblog.api.request.PostCreate;
import com.kakjziblog.api.request.PostEdit;
import com.kakjziblog.api.request.PostSearch;
import com.kakjziblog.api.response.PostResponse;
import com.kakjziblog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void get(@RequestBody @Valid PostCreate request) {

        request.validate();
        postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    /**
     * 조회 API
     * 여러개 글을 조회
     * /posts
     */
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        postService.edit(postId, postEdit);
    }
    @DeleteMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId) {
        postService.delete(postId);
    }

}